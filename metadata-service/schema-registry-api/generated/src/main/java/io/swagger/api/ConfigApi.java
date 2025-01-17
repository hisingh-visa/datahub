/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.33).
 * https://github.com/swagger-api/swagger-codegen Do not edit the class manually.
 */
package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.datahubproject.schema_registry.openapi.generated.Config;
import io.datahubproject.schema_registry.openapi.generated.ConfigUpdateRequest;
import io.datahubproject.schema_registry.openapi.generated.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
    date = "2022-12-20T16:52:36.517693Z[Europe/Lisbon]")
@Validated
public interface ConfigApi {

  Logger log = LoggerFactory.getLogger(ConfigApi.class);

  default Optional<ObjectMapper> getObjectMapper() {
    return Optional.empty();
  }

  default Optional<HttpServletRequest> getRequest() {
    return Optional.empty();
  }

  default Optional<String> getAcceptHeader() {
    return getRequest().map(r -> r.getHeader("Accept"));
  }

  @Operation(
      summary = "Delete subject compatibility level",
      description =
          "Deletes the specified subject-level compatibility level config and reverts to the global default.",
      tags = {"Config (v1)"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Operation succeeded. Returns old compatibility level.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = String.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found. Error code 40401 indicates subject not found.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ErrorMessage.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "Internal Server Error. Error code 50001 indicates a failure in the backend data store.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ErrorMessage.class)))
      })
  @RequestMapping(
      value = "/config/{subject}",
      produces = {
        "application/vnd.schemaregistry.v1+json",
        "application/vnd.schemaregistry+json; qs=0.9",
        "application/json; qs=0.5"
      },
      method = RequestMethod.DELETE)
  default ResponseEntity<String> deleteSubjectConfig(
      @Parameter(
              in = ParameterIn.PATH,
              description = "Name of the subject",
              required = true,
              schema = @io.swagger.v3.oas.annotations.media.Schema())
          @PathVariable("subject")
          String subject) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"NONE\"", String.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @Operation(
      summary = "Delete global compatibility level",
      description = "Deletes the global compatibility level config and reverts to the default.",
      tags = {"Config (v1)"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Operation succeeded. Returns old global compatibility level.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = String.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "Internal Server Error. Error code 50001 indicates a failure in the backend data store.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ErrorMessage.class)))
      })
  @RequestMapping(
      value = "/config",
      produces = {
        "application/vnd.schemaregistry.v1+json",
        "application/vnd.schemaregistry+json; qs=0.9",
        "application/json; qs=0.5"
      },
      method = RequestMethod.DELETE)
  default ResponseEntity<String> deleteTopLevelConfig() {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"NONE\"", String.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @Operation(
      summary = "Get subject compatibility level",
      description = "Retrieves compatibility level for a subject.",
      tags = {"Config (v1)"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The subject compatibility level.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = Config.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found. Error code 40401 indicates subject not found.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ErrorMessage.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "Internal Server Error. Error code 50001 indicates a failure in the backend data store.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ErrorMessage.class)))
      })
  @RequestMapping(
      value = "/config/{subject}",
      produces = {
        "application/vnd.schemaregistry.v1+json",
        "application/vnd.schemaregistry+json; qs=0.9",
        "application/json; qs=0.5"
      },
      method = RequestMethod.GET)
  default ResponseEntity<Config> getSubjectLevelConfig(
      @Parameter(
              in = ParameterIn.PATH,
              description = "Name of the subject",
              required = true,
              schema = @io.swagger.v3.oas.annotations.media.Schema())
          @PathVariable("subject")
          String subject,
      @Parameter(
              in = ParameterIn.QUERY,
              description =
                  "Whether to return the global compatibility level  if subject compatibility level not found",
              schema = @io.swagger.v3.oas.annotations.media.Schema())
          @Valid
          @RequestParam(value = "defaultToGlobal", required = false)
          Boolean defaultToGlobal) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper()
                  .get()
                  .readValue("{\n  \"compatibilityLevel\" : \"FULL_TRANSITIVE\"\n}", Config.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @Operation(
      summary = "Get global compatibility level",
      description = "Retrieves the global compatibility level.",
      tags = {"Config (v1)"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The global compatibility level.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = Config.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "Internal Server Error. Error code 50001 indicates a failure in the backend data store.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ErrorMessage.class)))
      })
  @RequestMapping(
      value = "/config",
      produces = {
        "application/vnd.schemaregistry.v1+json",
        "application/vnd.schemaregistry+json; qs=0.9",
        "application/json; qs=0.5"
      },
      method = RequestMethod.GET)
  default ResponseEntity<Config> getTopLevelConfig() {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper()
                  .get()
                  .readValue("{\n  \"compatibilityLevel\" : \"FULL_TRANSITIVE\"\n}", Config.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @Operation(
      summary = "Update subject compatibility level",
      description =
          "Update compatibility level for the specified subject. On success, echoes the original request back to the client.",
      tags = {"Config (v1)"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The original request.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ConfigUpdateRequest.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found. Error code 40401 indicates subject not found.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ErrorMessage.class))),
        @ApiResponse(
            responseCode = "422",
            description =
                "Unprocessable Entity. Error code 42203 indicates invalid compatibility level.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ErrorMessage.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "Internal Server Error. Error code 50001 indicates a failure in the backend data store. Error code 50003 indicates a failure forwarding the request to the primary.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ErrorMessage.class)))
      })
  @RequestMapping(
      value = "/config/{subject}",
      produces = {
        "application/vnd.schemaregistry.v1+json",
        "application/vnd.schemaregistry+json; qs=0.9",
        "application/json; qs=0.5"
      },
      consumes = {
        "application/vnd.schemaregistry.v1+json",
        "application/vnd.schemaregistry+json",
        "application/json",
        "application/octet-stream"
      },
      method = RequestMethod.PUT)
  default ResponseEntity<ConfigUpdateRequest> updateSubjectLevelConfig(
      @Parameter(
              in = ParameterIn.PATH,
              description = "Name of the subject",
              required = true,
              schema = @io.swagger.v3.oas.annotations.media.Schema())
          @PathVariable("subject")
          String subject,
      @Parameter(
              in = ParameterIn.DEFAULT,
              description = "Config Update Request",
              required = true,
              schema = @io.swagger.v3.oas.annotations.media.Schema())
          @Valid
          @RequestBody
          ConfigUpdateRequest body) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper()
                  .get()
                  .readValue(
                      "{\n  \"compatibility\" : \"FULL_TRANSITIVE\"\n}", ConfigUpdateRequest.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @Operation(
      summary = "Update global compatibility level",
      description =
          "Updates the global compatibility level. On success, echoes the original request back to the client.",
      tags = {"Config (v1)"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The original request.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ConfigUpdateRequest.class))),
        @ApiResponse(
            responseCode = "422",
            description =
                "Unprocessable Entity. Error code 42203 indicates invalid compatibility level.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ErrorMessage.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "Internal Server Error. Error code 50001 indicates a failure in the backend data store. Error code 50003 indicates a failure forwarding the request to the primary.",
            content =
                @Content(
                    mediaType = "application/vnd.schemaregistry.v1+json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ErrorMessage.class)))
      })
  @RequestMapping(
      value = "/config",
      produces = {
        "application/vnd.schemaregistry.v1+json",
        "application/vnd.schemaregistry+json; qs=0.9",
        "application/json; qs=0.5"
      },
      consumes = {
        "application/vnd.schemaregistry.v1+json",
        "application/vnd.schemaregistry+json",
        "application/json",
        "application/octet-stream"
      },
      method = RequestMethod.PUT)
  default ResponseEntity<ConfigUpdateRequest> updateTopLevelConfig(
      @Parameter(
              in = ParameterIn.DEFAULT,
              description = "Config Update Request",
              required = true,
              schema = @io.swagger.v3.oas.annotations.media.Schema())
          @Valid
          @RequestBody
          ConfigUpdateRequest body) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper()
                  .get()
                  .readValue(
                      "{\n  \"compatibility\" : \"FULL_TRANSITIVE\"\n}", ConfigUpdateRequest.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
