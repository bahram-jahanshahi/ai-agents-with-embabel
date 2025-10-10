package se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsProvider {

    @Bean
    ToolCallbackProvider toolCallbackProvider(TrendingTools tools) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(tools)
                .build();
    }
}
