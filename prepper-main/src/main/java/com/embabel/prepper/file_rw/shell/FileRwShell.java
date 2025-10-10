package com.embabel.prepper.file_rw.shell;

import com.embabel.agent.api.common.autonomy.AgentInvocation;
import com.embabel.agent.core.AgentPlatform;
import com.embabel.agent.core.ProcessOptions;
import com.embabel.prepper.file_rw.agent.FileRwDomain;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public record FileRwShell(AgentPlatform agentPlatform) {

    @ShellMethod("file-rw" )
    String fileRw() {
        return AgentInvocation.builder(agentPlatform)
                .options(ProcessOptions.builder().verbosity(v -> v.showPrompts(true)).build())
                .build(String.class)
                .invoke(new FileRwDomain.FileWriteRequest("embabel.txt", "firstContent"));

    }
}
