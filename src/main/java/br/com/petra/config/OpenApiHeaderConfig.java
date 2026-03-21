package br.com.petra.config;

import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.UUID;

@Configuration
public class OpenApiHeaderConfig {

    private static final String HEADER_NAME = "x-fapi-interaction-id";

    @Bean
    public OperationCustomizer fapiInteractionIdHeaderCustomizer() {

        return (operation, handlerMethod) -> {
            final String generatedUuid = UUID.randomUUID().toString();
            if (operation.getParameters() == null) {
                operation.setParameters(new ArrayList<>());
            }

            boolean headerAlreadyPresent = operation.getParameters().stream()
                    .anyMatch(parameter -> HEADER_NAME.equalsIgnoreCase(parameter.getName())
                            && "header".equalsIgnoreCase(parameter.getIn()));

            if (!headerAlreadyPresent) {
                StringSchema schema = new StringSchema();
                schema.setFormat("uuid");
                schema.setDefault(generatedUuid);
                schema.setExample(generatedUuid);

                Parameter parameter = new Parameter()
                        .name(HEADER_NAME)
                        .in("header")
                        .required(false)
                        .description("Correlation id opcional para rastreio da requisicao")
                        .schema(schema);

                operation.addParametersItem(parameter);
            }

            return operation;
        };
    }
}
