package com.linkedin.datahub.graphql.resolvers.mutate.util;


import com.linkedin.datahub.graphql.QueryContext;
import com.linkedin.entity.client.EntityClient;
import com.linkedin.metadata.Constants;
import com.linkedin.metadata.query.SearchFlags;
import com.linkedin.metadata.query.filter.Condition;
import com.linkedin.metadata.query.filter.ConjunctiveCriterion;
import com.linkedin.metadata.query.filter.ConjunctiveCriterionArray;
import com.linkedin.metadata.query.filter.Criterion;
import com.linkedin.metadata.query.filter.CriterionArray;
import com.linkedin.metadata.query.filter.Filter;
import com.linkedin.metadata.search.SearchResult;
import com.linkedin.r2.RemoteInvocationException;
import com.linkedin.schema.ArrayType;
import com.linkedin.schema.BooleanType;
import com.linkedin.schema.DateType;
import com.linkedin.schema.NumberType;
import com.linkedin.schema.SchemaFieldDataType;
import com.linkedin.schema.StringType;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.Objects;

@Slf4j
public class BusinessAttributeUtils {
    private static final Integer DEFAULT_START = 0;
    private static final Integer DEFAULT_COUNT = 1000;
    private static final String DEFAULT_QUERY = "";
    private static final String NAME_INDEX_FIELD_NAME = "name";

    private BusinessAttributeUtils() {
    }

    public static boolean hasNameConflict(String name, QueryContext context, EntityClient entityClient) {
        Filter filter = buildNameFilter(name);
        try {
            final SearchResult gmsResult = entityClient.search(
                    Constants.BUSINESS_ATTRIBUTE_ENTITY_NAME,
                    DEFAULT_QUERY,
                    filter,
                    null,
                    DEFAULT_START,
                    DEFAULT_COUNT,
                    context.getAuthentication(),
                    new SearchFlags().setFulltext(true));
            return gmsResult.getNumEntities() > 0;
        } catch (RemoteInvocationException e) {
            throw new RuntimeException("Failed to fetch Business Attributes", e);
        }
    }

    private static Filter buildNameFilter(String name) {
        return new Filter().setOr(
                new ConjunctiveCriterionArray(
                        new ConjunctiveCriterion().setAnd(buildNameCriterion(name))
                )
        );
    }

    private static CriterionArray buildNameCriterion(@Nonnull final String name) {
        return new CriterionArray(new Criterion()
                .setField(NAME_INDEX_FIELD_NAME)
                .setValue(name)
                .setCondition(Condition.EQUAL));
    }

    public static SchemaFieldDataType mapSchemaFieldDataType(com.linkedin.datahub.graphql.generated.SchemaFieldDataType type) {
        if (Objects.isNull(type)) {
            return null;
        }
        SchemaFieldDataType schemaFieldDataType = new SchemaFieldDataType();
        switch (type) {
            case BOOLEAN:
                schemaFieldDataType.setType(SchemaFieldDataType.Type.create(new BooleanType()));
                return schemaFieldDataType;
            case STRING:
                schemaFieldDataType.setType(SchemaFieldDataType.Type.create(new StringType()));
                return schemaFieldDataType;
            case NUMBER:
                schemaFieldDataType.setType(SchemaFieldDataType.Type.create(new NumberType()));
                return schemaFieldDataType;
            case DATE:
                schemaFieldDataType.setType(SchemaFieldDataType.Type.create(new DateType()));
                return schemaFieldDataType;
            case ARRAY:
                schemaFieldDataType.setType(SchemaFieldDataType.Type.create(new ArrayType()));
                return schemaFieldDataType;
            default:
                return null;
        }
    }
}
