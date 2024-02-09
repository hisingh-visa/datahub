package com.linkedin.metadata.search.elasticsearch.query.request;

import com.linkedin.metadata.models.AspectSpec;
import com.linkedin.metadata.models.EntitySpec;
import com.linkedin.metadata.models.SearchableFieldSpec;
import com.linkedin.metadata.models.SearchableRefFieldSpec;
import com.linkedin.metadata.models.annotation.SearchableAnnotation;
import com.linkedin.metadata.models.annotation.SearchableRefAnnotation;
import com.linkedin.metadata.models.registry.EntityRegistry;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.linkedin.metadata.Constants.SKIP_REFERENCE_ASPECT;
import static com.linkedin.metadata.search.elasticsearch.indexbuilder.SettingsBuilder.*;


@Builder
@Getter
@Accessors(fluent = true)
@EqualsAndHashCode
public class SearchFieldConfig {
    public static final float DEFAULT_BOOST = 1.0f;

    public static final Set<String> KEYWORD_FIELDS = Set.of("urn", "runId", "_index");
    public static final Set<String> PATH_HIERARCHY_FIELDS = Set.of("browsePathV2");

    // These should not be used directly since there is a specific
    // order in which these rules need to be evaluated for exceptions to
    // the rules.
    private static final Set<SearchableAnnotation.FieldType> TYPES_WITH_DELIMITED_SUBFIELD =
            Set.of(
                    SearchableAnnotation.FieldType.TEXT,
                    SearchableAnnotation.FieldType.TEXT_PARTIAL,
                    SearchableAnnotation.FieldType.WORD_GRAM
                    // NOT URN_PARTIAL (urn field is special)
            );
    // NOT comprehensive
    private static final Set<SearchableAnnotation.FieldType> TYPES_WITH_KEYWORD_SUBFIELD =
            Set.of(
                    SearchableAnnotation.FieldType.URN,
                    SearchableAnnotation.FieldType.KEYWORD,
                    SearchableAnnotation.FieldType.URN_PARTIAL
            );
    private static final Set<SearchableAnnotation.FieldType> TYPES_WITH_BROWSE_PATH =
            Set.of(
                    SearchableAnnotation.FieldType.BROWSE_PATH
            );
    private static final Set<SearchableAnnotation.FieldType> TYPES_WITH_BROWSE_PATH_V2 =
            Set.of(
                    SearchableAnnotation.FieldType.BROWSE_PATH_V2
            );
    private static final Set<SearchableAnnotation.FieldType> TYPES_WITH_BASE_KEYWORD =
            Set.of(
                    SearchableAnnotation.FieldType.TEXT,
                    SearchableAnnotation.FieldType.TEXT_PARTIAL,
                    SearchableAnnotation.FieldType.KEYWORD,
                    SearchableAnnotation.FieldType.WORD_GRAM,
                    // not analyzed
                    SearchableAnnotation.FieldType.BOOLEAN,
                    SearchableAnnotation.FieldType.COUNT,
                    SearchableAnnotation.FieldType.DATETIME,
                    SearchableAnnotation.FieldType.OBJECT
            );
    // NOT true for `urn`
    public static final Set<SearchableAnnotation.FieldType> TYPES_WITH_URN_TEXT =
            Set.of(
                    SearchableAnnotation.FieldType.URN,
                    SearchableAnnotation.FieldType.URN_PARTIAL
            );

    public static final Set<SearchableAnnotation.FieldType> TYPES_WITH_WORD_GRAM =
        Set.of(
            SearchableAnnotation.FieldType.WORD_GRAM
        );

    @Nonnull
    private final String fieldName;
    @Nonnull
    private final String shortName;
    @Builder.Default
    private final Float boost = DEFAULT_BOOST;
    private final String analyzer;
    private boolean hasKeywordSubfield;
    private boolean hasDelimitedSubfield;
    private boolean hasWordGramSubfields;
    private boolean isQueryByDefault;
    private boolean isDelimitedSubfield;
    private boolean isKeywordSubfield;
    private boolean isWordGramSubfield;
//    private boolean isReferenceField;

    public static SearchFieldConfig detectSubFieldType(@Nonnull SearchableFieldSpec fieldSpec) {
        final SearchableAnnotation searchableAnnotation = fieldSpec.getSearchableAnnotation();
        final String fieldName = searchableAnnotation.getFieldName();
        final float boost = (float) searchableAnnotation.getBoostScore();
        final SearchableAnnotation.FieldType fieldType = searchableAnnotation.getFieldType();
        return detectSubFieldType(fieldName, boost, fieldType, searchableAnnotation.isQueryByDefault());
    }

    public static Set<SearchFieldConfig> detectSubFieldType(@Nonnull SearchableRefFieldSpec fieldSpec,
                                                            int depth, EntityRegistry entityRegistry) {
        Set<SearchFieldConfig> fieldConfigs = new HashSet<>();
        final SearchableRefAnnotation searchableRefAnnotation = fieldSpec.getSearchableRefAnnotation();
        String fieldName = searchableRefAnnotation.getFieldName();
        final float boost = (float) searchableRefAnnotation.getBoostScore();
        final SearchableAnnotation.FieldType fieldType = searchableRefAnnotation.getFieldType();
        fieldConfigs.addAll(detectSubFieldType(fieldSpec, depth , entityRegistry, boost, ""));
        return fieldConfigs;
    }

    public static Set<SearchFieldConfig> detectSubFieldType(@Nonnull SearchableRefFieldSpec refFieldSpec, int depth,
                                                            EntityRegistry entityRegistry, float boost,
                                                            String prefixFieldName) {
        Set<SearchFieldConfig> fieldConfigs = new HashSet<>();
        final SearchableRefAnnotation searchableRefAnnotation = refFieldSpec.getSearchableRefAnnotation();
        EntitySpec refEntitySpec = entityRegistry.getEntitySpec(searchableRefAnnotation.getRefType());
        String fieldName = searchableRefAnnotation.getFieldName();
        final SearchableAnnotation.FieldType fieldType = searchableRefAnnotation.getFieldType();
        if(!prefixFieldName.isEmpty()) {
            fieldName = prefixFieldName + "." + fieldName;
        }

        if (depth == 0) {
            fieldConfigs.add(detectSubFieldType(fieldName, boost, fieldType, searchableRefAnnotation.isQueryByDefault()));
            return fieldConfigs;
        }

        List<AspectSpec> aspectSpecs = refEntitySpec.getAspectSpecs();

        for (AspectSpec aspectSpec : aspectSpecs) {
             if (!SKIP_REFERENCE_ASPECT.contains(aspectSpec.getName())) {
                for (SearchableFieldSpec searchableFieldSpec : aspectSpec.getSearchableFieldSpecs()) {
                    String refFieldName = searchableFieldSpec.getSearchableAnnotation().getFieldName();
                    refFieldName = fieldName + "." + refFieldName;

                    final SearchableAnnotation searchableAnnotation = searchableFieldSpec.getSearchableAnnotation();
                    final float refBoost = (float) searchableAnnotation.getBoostScore() * boost;
                    final SearchableAnnotation.FieldType refFieldType = searchableAnnotation.getFieldType();
                    fieldConfigs.add(detectSubFieldType(refFieldName, refBoost, refFieldType, searchableAnnotation.isQueryByDefault()));
                }

                for (SearchableRefFieldSpec searchableRefFieldSpec : aspectSpec.getSearchableRefFieldSpecs()) {
                    String refFieldName = searchableRefFieldSpec.getSearchableRefAnnotation().getFieldName();
                    refFieldName = fieldName + "." + refFieldName;
                    final float refBoost = (float) searchableRefFieldSpec.getSearchableRefAnnotation().getBoostScore() * boost;
                    fieldConfigs.addAll(detectSubFieldType(searchableRefFieldSpec, depth - 1, entityRegistry, refBoost, refFieldName));
                }
            }
        }

        return fieldConfigs;
    }



    public static SearchFieldConfig detectSubFieldType(String fieldName,
                                                       SearchableAnnotation.FieldType fieldType,
                                                       boolean isQueryByDefault) {
        return detectSubFieldType(fieldName, DEFAULT_BOOST, fieldType, isQueryByDefault);
    }

    public static SearchFieldConfig detectSubFieldType(String fieldName,
                                                       float boost,
                                                       SearchableAnnotation.FieldType fieldType,
                                                       boolean isQueryByDefault) {
        return SearchFieldConfig.builder()
                .fieldName(fieldName)
                .boost(boost)
                .analyzer(getAnalyzer(fieldName, fieldType))
                .hasKeywordSubfield(hasKeywordSubfield(fieldName, fieldType))
                .hasDelimitedSubfield(hasDelimitedSubfield(fieldName, fieldType))
                .hasWordGramSubfields(hasWordGramSubfields(fieldName, fieldType))
                .isQueryByDefault(isQueryByDefault)
                .build();
    }
    public static Set<SearchFieldConfig> getStandardFields(EntitySpec entitySpec, int depth) {
        Set<SearchFieldConfig> fieldConfigs = new HashSet<>();

        // Generate a SearchFieldConfig for each field in the entity spec
        for (SearchableFieldSpec fieldSpec : entitySpec.getSearchableFieldSpecs()) {
            SearchFieldConfig fieldConfig = detectSubFieldType(fieldSpec);
            fieldConfigs.add(fieldConfig);
        }

        // If the depth is greater than 0, generate SearchFieldConfig objects for the fields of any referenced entities
        if (depth > 0) {
            for (SearchableRefFieldSpec refFieldSpec : entitySpec.getSearchableRefFieldSpecs()) {
//                SearchFieldConfig refFieldConfig = detectSubFieldType(refFieldSpec, depth - 1);
//                fieldConfigs.add(refFieldConfig);
            }
        }
        return fieldConfigs;
    }
    public boolean isKeyword() {
        return KEYWORD_ANALYZER.equals(analyzer()) || isKeyword(fieldName());
    }

    private static boolean hasDelimitedSubfield(String fieldName, SearchableAnnotation.FieldType fieldType) {
        return !fieldName.contains(".")
                && ("urn".equals(fieldName) || TYPES_WITH_DELIMITED_SUBFIELD.contains(fieldType));
    }

    private static boolean hasWordGramSubfields(String fieldName, SearchableAnnotation.FieldType fieldType) {
        return !fieldName.contains(".")
            && (TYPES_WITH_WORD_GRAM.contains(fieldType));
    }
    private static boolean hasKeywordSubfield(String fieldName, SearchableAnnotation.FieldType fieldType) {
        return !"urn".equals(fieldName)
                && !fieldName.contains(".")
                && (TYPES_WITH_DELIMITED_SUBFIELD.contains(fieldType) // if delimited then also has keyword
                    || TYPES_WITH_KEYWORD_SUBFIELD.contains(fieldType));
    }
    private static boolean isKeyword(String fieldName) {
        return fieldName.endsWith(".keyword")
                || KEYWORD_FIELDS.contains(fieldName);
    }

    private static String getAnalyzer(String fieldName, SearchableAnnotation.FieldType fieldType) {
        // order is important
        if (TYPES_WITH_BROWSE_PATH.contains(fieldType)) {
            return BROWSE_PATH_HIERARCHY_ANALYZER;
        } else if (TYPES_WITH_BROWSE_PATH_V2.contains(fieldType)) {
            return BROWSE_PATH_V2_HIERARCHY_ANALYZER;
        // sub fields
        } else if (isKeyword(fieldName)) {
            return KEYWORD_ANALYZER;
        } else if (fieldName.endsWith(".delimited")) {
            return TEXT_SEARCH_ANALYZER;
        // non-subfield cases below
        } else if (TYPES_WITH_BASE_KEYWORD.contains(fieldType)) {
            return KEYWORD_ANALYZER;
        } else if (TYPES_WITH_URN_TEXT.contains(fieldType)) {
            return URN_SEARCH_ANALYZER;
        } else {
            throw new IllegalStateException(String.format("Unknown analyzer for fieldName: %s, fieldType: %s", fieldName, fieldType));
        }
    }

    public static class SearchFieldConfigBuilder {
        public SearchFieldConfigBuilder fieldName(@Nonnull String fieldName) {
            this.fieldName = fieldName;
            isDelimitedSubfield(fieldName.endsWith(".delimited"));
            isKeywordSubfield(fieldName.endsWith(".keyword"));
            isWordGramSubfield(fieldName.contains("wordGrams"));
            shortName(fieldName.split("[.]")[0]);
            return this;
        }
    }
}
