package se.bahram.ai.mcp.github_official_mcp_client_with_java.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ChatClientConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder, ToolCallbackProvider tools) {

        explore(tools);

        // Give the LLM access to all MCP tools
        return builder
                .defaultSystem("""
                        You are a precise assistant. When asked to retrieve files from GitHub,
                        use the GitHub MCP tool to read repo files and then summarize clearly.
                        """)
                .defaultToolCallbacks(tools)
                .build();
    }

    private void explore(ToolCallbackProvider tools) {
        log.info("Available tools:");
        var toolCallBacks = tools.getToolCallbacks();
        for (ToolCallback toolCallBack : toolCallBacks) {
            log.info(" - {}, {}, {}",
                    toolCallBack.getToolMetadata(),
                    toolCallBack.getToolDefinition().name(),
                    toolCallBack.getToolDefinition().description());
        }
    }
}
