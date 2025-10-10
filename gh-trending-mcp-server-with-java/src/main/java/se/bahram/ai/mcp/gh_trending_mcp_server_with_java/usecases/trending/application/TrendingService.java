package se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.application;

import org.springframework.stereotype.Service;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Developer;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Language;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Repo;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.SpokenLanguage;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.port.TrendingPort;

import java.util.List;

@Service
public record TrendingService(TrendingPort port) {

    public List<Repo> repos(String language, String since, String spokenLanguage) {
        return port.fetchRepos(blankToNull(language), defaultSince(since), blankToNull(spokenLanguage));
    }

    public List<Developer> developers(String language, String since) {
        return port.fetchDevelopers(blankToNull(language), defaultSince(since));
    }

    public List<Language> languages() { return port.listLanguages(); }

    public List<SpokenLanguage> spokenLanguages() { return port.listSpokenLanguages(); }

    private static String defaultSince(String v) { return (v == null || v.isBlank()) ? "daily" : v; }
    private static String blankToNull(String v) { return (v == null || v.isBlank()) ? null : v; }
}
