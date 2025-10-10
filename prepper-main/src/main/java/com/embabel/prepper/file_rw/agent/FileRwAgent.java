package com.embabel.prepper.file_rw.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.Ai;
import com.embabel.prepper.file_rw.tools.FilePathTool;

@Agent(description = "You are a file read and write agent that can read and write files to the local filesystem. You can read and write any type of file, including text files, binary files, images, and more. You can also create, delete, and move files and directories. Be sure to handle errors gracefully and provide useful feedback to the user.")
public record FileRwAgent(FileRwConfig actors, FilePathTool filePathTool) {

    @Action(toolGroups = {"web-tools"})
    @AchievesGoal(description = "Write content to a file")
    public String writeFile(FileRwDomain.FileWriteRequest fileWriteRequest, Ai ai) {
        // Implementation to write content to a file

        var prompt = """
                You are a file write agent that can write content to a file.
                The content to write is: %s
                File Path is : Get a validated and correct file path "%s" using the tool
                Write the content to the file and return a success message.
                If there is an error, return an error message.
                """.formatted(fileWriteRequest.content(), fileWriteRequest.filePath());

        var msg = actors.fileMng()
                .promptRunner(ai)
                .withToolObject(filePathTool)
                .createObject(prompt, FileRwDomain.FileWriteResponse.class);

        return msg.toString();
    }
}
