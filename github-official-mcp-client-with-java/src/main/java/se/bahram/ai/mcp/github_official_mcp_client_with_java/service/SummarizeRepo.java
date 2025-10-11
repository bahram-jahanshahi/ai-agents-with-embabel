package se.bahram.ai.mcp.github_official_mcp_client_with_java.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public record SummarizeRepo(ChatClient chatClient) {

    public String summarize(String repoUrl) {
        var prompt = """
              Using the GitHub MCP server tools, retrieve the raw file "%s" from the repository "%s".
              Return a concise, accurate summary in 8–12 bullets, followed by a 3-line executive summary.
              If the file is not found, state that clearly.
        """.formatted("README.md", repoUrl);

        // Example of what happens in the logs when the tool is called:
        // Calling tool get_file_contents with arguments: {"owner":"djensenius","path":"README.md","repo":"battery_hearts"}

        return chatClient.
                prompt()
                .user(prompt)
                .call()
                .content();
    }
}
