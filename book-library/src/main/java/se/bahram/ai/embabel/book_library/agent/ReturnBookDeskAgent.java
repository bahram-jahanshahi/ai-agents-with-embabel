package se.bahram.ai.embabel.book_library.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;

@Agent(
        name = "Return Book Desk Agent",
        description = "An agent that helps users return books to the library."
)
public class ReturnBookDeskAgent {

    @Action
    ReturnBookRequest processRequest(UserInput userInput, OperationContext context) {
        var prompt = """
                You are a helpful library assistant.
                A user wants to return a book.
                Extract the book ID and member ID from the user's input.
                If the information is not available, respond with "Unknown".
                
                User Input: %s
                
                """.formatted(userInput.getContent());
        return context
                .promptRunner()
                .createObject(prompt, ReturnBookRequest.class);
    }

    @AchievesGoal(
            description = "Handles the return of a book to the library."
    )
    @Action
    String returnBook(ReturnBookRequest returnBookRequest) {
        return "Your member ID " + returnBookRequest.memberId() +
                " has successfully returned the book with ID " + returnBookRequest.bookId() + ". Thank you!";
    }

    public record ReturnBookRequest(String bookId, String memberId) {}
}
