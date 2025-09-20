package se.bahram.ai.embabel.simple_work_flow.template.agents;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.prompt.persona.Persona;
import org.springframework.context.annotation.Profile;

@Agent(description = "Draw a plan to train a sport student how to play badminton and review the training plan")
@Profile("!test")
public class TrainBadmintonAgent {

    @AchievesGoal(
            description = "The student learns how to smash"
    )
    @Action
    String reviewTrainingPlan(UserInput userInput, String trainingPlan, OperationContext context) {
        var review = context
                .ai()
                .withAutoLlm()
                .withPromptContributor(new Persona("Badminton Plan Reviewer", "Badminton Plan Reviewer", "Professional and insightful", "Review the badminton plan and check student can learn smash") )
                .generateText("""
                        You are a badminton Plan Reviewer. Review the following training plan a coach to learn how to smash in badminton.
                        
                        Training Plan:
                        %s
                        
                        Provide constructive feedback and suggest improvements if necessary.
                        """.formatted(trainingPlan)
                ).trim();

        return  review;
    }

    @Action
    String createTrainingPlan(UserInput userInput, OperationContext context) {
        var trainingPlan = context
                .ai()
                .withAutoLlm()
                .withPromptContributor(new Persona("Badminton Coach", "Professional and encouraging", "Helps students improve their badminton skills", "Has coached several national level players") )
                .generateText("""
                        You are a badminton coach. Create a detailed training plan for a student who wants to learn how to smash in badminton.
                        
                        The training plan should include:
                        1. Warm-up exercises
                        2. Drills to practice the smash technique
                        3. Cool-down exercises
                        4. Tips for improving power and accuracy
                        
                        Provide the training plan in a clear and structured format.
                        """).trim();

        return trainingPlan;
    }
}
