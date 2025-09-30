package se.bahram.ai.embabel.book_library;

import com.embabel.agent.config.annotation.EnableAgentShell;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableAgentMcpServer
@EnableAgentShell
public class BookLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookLibraryApplication.class, args);
	}

}
