package com.embabel.prepper.file_rw.tools;

import com.embabel.agent.core.ToolGroup;
import com.embabel.agent.core.ToolGroupDescription;
import com.embabel.agent.core.ToolGroupPermission;
import com.embabel.agent.tools.mcp.McpToolGroup;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Configuration
public class ToolGroupsConfig {

    private final List<McpSyncClient> mcpSyncClients;

    public ToolGroupsConfig(List<McpSyncClient> mcpSyncClients) {
        this.mcpSyncClients = mcpSyncClients;
    }

    @Bean(name = "webFileTools")
    public ToolGroup webFileTools() {
        // The third arg ("file-mcp-server") should match the connection name in application.yml
        return new McpToolGroup(
                ToolGroupDescription.Companion.invoke(
                        "Tools exposed by the remote MCP server (SSE)",
                        "web-tools"
                ),
                "SSE",       // transport label (informative; Embabel matches by server name)
                "file-mcp-server",          // serverConfigurationName from your YAML
                Set.of(ToolGroupPermission.INTERNET_ACCESS),
                mcpSyncClients,
                // Optional: filter which tools from that MCP server are included in this group
                cb -> true             // or e.g. cb.getToolDefinition().name().contains("search")
        );
    }

}
