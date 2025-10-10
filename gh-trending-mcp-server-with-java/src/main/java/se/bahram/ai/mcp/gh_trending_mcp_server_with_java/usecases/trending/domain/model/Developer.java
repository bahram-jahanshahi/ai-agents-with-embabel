package se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model;

public record Developer(
        String username,
        String name,
        String url,
        String repoName,
        String repoUrl,
        String repoDescription
) {
}
