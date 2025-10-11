package se.bahram.ai.mcp.github_official_mcp_client_with_java.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.bahram.ai.mcp.github_official_mcp_client_with_java.service.SummarizeRepo;

@RestController
@RequestMapping("/api")
public record SummarizeRepoController(SummarizeRepo summarizeRepo) {

    @GetMapping("/summarize-repo")
    public ResponseEntity<String> summarize() {

        var repoUrl = "https://github.com/djensenius/battery_hearts";

        return ResponseEntity.ok(summarizeRepo.summarize(repoUrl));
    }
}
