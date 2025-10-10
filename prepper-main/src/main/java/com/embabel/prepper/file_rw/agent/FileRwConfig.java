package com.embabel.prepper.file_rw.agent;

import com.embabel.agent.prompt.persona.Actor;
import com.embabel.agent.prompt.persona.RoleGoalBackstory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "file-rw")
@Validated
public record FileRwConfig(
        @NotNull Actor<RoleGoalBackstory> fileMng
        ) {
}
