package br.com.petra.config.observability;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationPredicate;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;

@Configuration
public class ActuatorObservationFilterConfig {

    @Bean
    ObservationPredicate skipActuatorFromHttpServerObservation() {
        return (observationName, context) -> !isActuatorRequest(context);
    }

    private boolean isActuatorRequest(Observation.Context context) {
        if (!(context instanceof ServerRequestObservationContext serverContext)) {
            return false;
        }

        HttpServletRequest request = serverContext.getCarrier();
        if (request == null) {
            return false;
        }

        String path = request.getRequestURI();
        return StringUtils.isNotBlank(path) && path.startsWith("/actuator");
    }
}

