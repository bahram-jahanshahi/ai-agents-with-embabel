package se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model;

import java.util.List;

public record Repo(
        String fullname,
        String url,
        String description,
        String language,
        Integer stars,
        Integer forks,
        Integer currentPeriodStars,
        List<String> builtBy // usernames or avatar urls if you want
) {
}
