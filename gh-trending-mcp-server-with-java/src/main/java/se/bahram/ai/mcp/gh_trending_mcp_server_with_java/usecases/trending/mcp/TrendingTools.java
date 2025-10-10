package se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.mcp;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.application.TrendingService;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Developer;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Language;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Repo;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.SpokenLanguage;

import java.util.List;

@Service
@Validated
public class TrendingTools {

    private final TrendingService service;
    public TrendingTools(TrendingService service) { this.service = service; }

    // --- MCP tool: repos ---
    @Tool(name = "repos",
            description = "Get GitHub Trending repositories. Omit language for 'all languages'. " +
                    "Spoken language may be code ('en') or name ('English').")
    public List<Repo> repos(
            @ToolParam(description = "Programming language (e.g., 'python'). Optional.") String language,
            @ToolParam(description = "since must be daily|weekly|monthly") String since,
            @ToolParam(description = "Spoken language code or name, e.g., 'en' Optional.") String spokenLanguage
    ) {
        return service.repos(language, since, spokenLanguage);
    }

    // --- MCP tool: developers ---
    @Tool(name = "developers",
            description = "Get GitHub Trending developers. Omit language for 'all languages'.")
    public List<Developer> developers(
            @ToolParam(description = "Programming language (optional).") String language,
            @ToolParam(description = "since must be daily|weekly|monthly") String since
    ) {
        return service.developers(language, since);
    }

    // --- MCP tool: languages ---
    @Tool(name = "languages", description = "List supported GitHub Trending languages.")
    public List<Language> languages() { return service.languages(); }

    // --- MCP tool: spoken-languages ---
    @Tool(name = "spokenLanguages", description = "List supported spoken languages (code + names).")
    public List<SpokenLanguage> spokenLanguages() { return service.spokenLanguages(); }
}
