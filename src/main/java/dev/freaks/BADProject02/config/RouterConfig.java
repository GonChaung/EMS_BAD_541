package dev.freaks.BADProject02.config;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class RouterConfig {
    @Bean
    public Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> router() {
        return request -> {
            String body = request.getBody();
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            if (body != null && !body.isEmpty()) {
                response.setBody("Processed request with body: " + body);
            } else {
                response.setBody("Processed request with no body");
            }
            response.setStatusCode(200);
            response.setIsBase64Encoded(false);
            return response;
        };
    }
}