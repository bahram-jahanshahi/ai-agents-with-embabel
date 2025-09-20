package se.bahram.ai.embabel.simple_work_flow.template;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import se.bahram.ai.embabel.simple_work_flow.template.injected.InjectedDemo;

@ShellComponent
public record DemoShell(InjectedDemo injectedDemo) {

    @ShellMethod("Invent an animal" )
    public String inventAnimal(String name) {
        var animal = injectedDemo.inventAnimal(name);
        return "You invented a " + animal.name() + " that goes '" + animal.sound() + "'";
    }
}
