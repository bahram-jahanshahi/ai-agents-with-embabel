package com.embabel.prepper.github_trending_discovery.agent;

import java.util.List;

public abstract class TrendingDiscoveryDomain {

    public record DiscoveryRequest(String spokenLanguage, String dateRange) {}

    public record GitHubTrendingProject(String name, String description, String url, int stars, int forks, String language) {}

    public record TodayGitHubTrendingProject(List<GitHubTrendingProject> projects) {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Today's GitHub Trending Projects:\n");
            for (GitHubTrendingProject project : projects) {
                sb.append(String.format("- %s (%s): %s [⭐ %d | 🍴 %d | 📝 %s]\n",
                        project.name(), project.language(), project.description(), project.stars(), project.forks(), project.url()));
            }
            return sb.toString();
        }
    }
}
