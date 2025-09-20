package se.bahram.ai.embabel.simple_work_flow.template.injected;

import com.embabel.agent.api.common.Ai;
import org.springframework.stereotype.Component;

@Component
public record InjectedDemo(Ai ai) {

    public record Animal(String name, String sound) {}

    public Animal inventAnimal(String name) {
        String prompt = """
               You just woke up in a magical forest.
               Invent a fictional animal.
               The animal should have a name and a species.
            """;

        return ai
                .withDefaultLlm()
                .createObject(prompt, Animal.class);
    }
}
