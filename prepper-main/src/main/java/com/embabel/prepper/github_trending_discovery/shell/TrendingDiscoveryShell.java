package com.embabel.prepper.github_trending_discovery.shell;

import com.embabel.agent.api.common.autonomy.AgentInvocation;
import com.embabel.agent.core.AgentPlatform;
import com.embabel.agent.core.ProcessOptions;
import com.embabel.prepper.github_trending_discovery.agent.TrendingDiscoveryDomain;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public record TrendingDiscoveryShell(AgentPlatform agentPlatform) {

    @ShellMethod("github-trending")
    String githubTrending() {

        var discoveryRequest = new TrendingDiscoveryDomain.DiscoveryRequest("English", "daily");

        var projects = AgentInvocation.builder(agentPlatform)
                .options(ProcessOptions.builder().verbosity(v -> v.showPrompts(true)).build())
                .build(TrendingDiscoveryDomain.TodayGitHubTrendingProject.class)
                .invoke(discoveryRequest);

        return projects.toString();
    }
}
