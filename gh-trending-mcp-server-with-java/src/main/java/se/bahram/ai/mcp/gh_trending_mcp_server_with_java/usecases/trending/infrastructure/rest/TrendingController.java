package se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.infrastructure.rest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.application.TrendingService;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Developer;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Language;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Repo;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.SpokenLanguage;

import java.util.List;

/**
 * REST controller for testing the Trending endpoints over HTTP.
 * Uses the same service that powers the MCP tools.
 */
@RestController
@RequestMapping("/api/trending")
public class TrendingController {

    private final TrendingService service;

    public TrendingController(TrendingService service) {
        this.service = service;
    }

    // --------------------------
    // Trending Repositories
    // --------------------------
    @GetMapping("/repos")
    public ResponseEntity<List<Repo>> getRepos(
            @RequestParam(required = false) String language,
            @RequestParam(defaultValue = "daily") String since,
            @RequestParam(name = "spoken_language", required = false) String spokenLanguage
    ) {
        List<Repo> repos = service.repos(language, since, spokenLanguage);
        return ResponseEntity.ok(repos);
    }

    // --------------------------
    // Trending Developers
    // --------------------------
    @GetMapping("/developers")
    public ResponseEntity<List<Developer>> getDevelopers(
            @RequestParam(required = false) String language,
            @RequestParam(defaultValue = "daily") String since
    ) {
        List<Developer> devs = service.developers(language, since);
        return ResponseEntity.ok(devs);
    }

    // --------------------------
    // Supported Programming Languages
    // --------------------------
    @GetMapping("/languages")
    public ResponseEntity<List<Language>> getLanguages() {
        return ResponseEntity.ok(service.languages());
    }

    // --------------------------
    // Supported Spoken Languages
    // --------------------------
    @GetMapping("/spoken-languages")
    public ResponseEntity<List<SpokenLanguage>> getSpokenLanguages() {
        return ResponseEntity.ok(service.spokenLanguages());
    }
}
