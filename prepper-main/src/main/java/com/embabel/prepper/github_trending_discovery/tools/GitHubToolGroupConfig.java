package com.embabel.prepper.github_trending_discovery.tools;

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
public class GitHubToolGroupConfig {

    private final List<McpSyncClient> mcpSyncClients;

    public GitHubToolGroupConfig(List<McpSyncClient> mcpSyncClients) {
        this.mcpSyncClients = mcpSyncClients;
    }

    @Bean(name = "githubTools")
    public ToolGroup githubTools() {
        return new McpToolGroup(
                ToolGroupDescription.Companion.invoke(
                        "Tools exposed by the remote MCP server (SSE)",
                        "github-tools"
                ),
                "SSE",       // transport label (informative; Embabel matches by server name)
                "github-trending-mcp-server",          // serverConfigurationName from your YAML
                Set.of(ToolGroupPermission.INTERNET_ACCESS),
                mcpSyncClients,
                // Optional: filter which tools from that MCP server are included in this group
                cb -> true             // or e.g. cb.getToolDefinition().name().contains("search")
        );
    }

}
