package com.embabel.prepper.github_trending_discovery.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.Ai;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Agent(description = "A GitHub trends project explorer that help users to explore the new trending projects every day")
public record TrendingDiscoveryAgent(TrendingDiscoveryConfig actors) {

    private final static Logger logger = LoggerFactory.getLogger(TrendingDiscoveryAgent.class);

    public TrendingDiscoveryAgent {
        logger.info("Initialized TrendingDiscoveryAgent with config: {}", actors);
    }

    @Action(toolGroups = {"github-tools"})
    @AchievesGoal(description = "Find all trending projects on GitHub")
    public TrendingDiscoveryDomain.TodayGitHubTrendingProject findTodayTrendingProject(
            TrendingDiscoveryDomain.DiscoveryRequest discoveryRequest,
            Ai ai) {

        var prompt = """
                You are a GitHub trending projects finder agent that can find the trending projects on GitHub.
                Spoken Language is %s and Date Range is %s.
                Fetch all the projects on this page and return the result
                """.formatted(discoveryRequest.spokenLanguage(), discoveryRequest.dateRange());
        logger.info("Action Prompt for Finding Today Trending Projects: {}", prompt);

        var projects = actors.githubTrendingProjectsFinder()
                .promptRunner(ai)
                .createObject(prompt, TrendingDiscoveryDomain.TodayGitHubTrendingProject.class);

        logger.info("Result for Today Trending Projects: {}", projects);

        return projects;
    }
}
