package se.bahram.ai.embabel.simple_work_flow;

import com.embabel.agent.config.annotation.EnableAgents;
import com.embabel.agent.config.annotation.LoggingThemes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAgents(loggingTheme = LoggingThemes.SEVERANCE)
public class SimpleWorkFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleWorkFlowApplication.class, args);
	}

}
