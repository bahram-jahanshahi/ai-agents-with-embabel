package se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.port;

import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Developer;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Language;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Repo;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.SpokenLanguage;

import java.util.List;

public interface TrendingPort {
    List<Repo> fetchRepos(String language, String since, String spokenLanguage);
    List<Developer> fetchDevelopers(String language, String since);
    List<Language> listLanguages();
    List<SpokenLanguage> listSpokenLanguages();
}
