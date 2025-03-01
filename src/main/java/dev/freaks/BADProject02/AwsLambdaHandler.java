package dev.freaks.BADProject02;

import dev.freaks.BADProject02.config.RouterConfig;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class AwsLambdaHandler extends SpringBootRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public AwsLambdaHandler() {
        super(RouterConfig.class); // Pass the class where the "router" function is defined
    }
}
