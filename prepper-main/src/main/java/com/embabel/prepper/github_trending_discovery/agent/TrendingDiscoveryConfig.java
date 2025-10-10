package com.embabel.prepper.github_trending_discovery.agent;

import com.embabel.agent.prompt.persona.Actor;
import com.embabel.agent.prompt.persona.RoleGoalBackstory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "trending-discovery")
@Validated
public record TrendingDiscoveryConfig(
        @NotNull Actor<RoleGoalBackstory> githubTrendingProjectsFinder,
        @NotNull Actor<RoleGoalBackstory> projectAnalyzer,
        @NotNull Actor<RoleGoalBackstory> reportWriter,
        @DefaultValue("8") int maxConcurrency
        ) {
}
