package se.bahram.ai.embabel.book_library.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.annotation.Condition;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;

import java.util.Date;

@Agent(
        name = "Return Book Desk Agent",
        description = "An agent that helps users return books to the library."
)
public class ReturnBookDeskAgent {

    // 1) Parse user input -> ReturnBookRequest
    //    We *optionally* advertise which postconditions this action may satisfy.
    @Action(post = {"hasBookId", "hasMemberId"})
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

    // 2) Preconditions (side-effect free!)
    // ✅ Condition to ensure bookId and memberID are known
    @Condition
    boolean essentialsKnown(ReturnBookRequest req) {
        return req != null
                && req.bookId() != null && !req.bookId().equalsIgnoreCase("Unknown")
                && req.memberId() != null && !req.memberId().equalsIgnoreCase("Unknown");
    }

    // 3) Gate the next step on BOTH conditions.
    //    If either is false, the process enters WAITING and the shell asks the user again.
    @Action(pre = {"essentialsKnown"})
    BorrowCard retrieveBorrowCard(ReturnBookRequest returnBookRequest) {
        // Simulate retrieving a borrow card from a database
        return new BorrowCard(returnBookRequest.bookId(),
                returnBookRequest.memberId(),
                new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000),
                new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000),
                new Date(System.currentTimeMillis()));
    }

    @Action
    LateFee processLateFee(BorrowCard borrowCard) {
        long lateDays = (new Date().getTime() - borrowCard.dueDate().getTime()) / (1000 * 60 * 60 * 24);
        double fee = lateDays * -1.0; // $1 per late day
        return new LateFee(fee, lateDays);
    }

    @Action
    ReturnBookReceipt generateReceipt(BorrowCard borrowCard, LateFee lateFee) {
        return new ReturnBookReceipt(borrowCard.memberId, borrowCard.bookId(), lateFee.fee());
    }

    @AchievesGoal(
            description = "Handles the return of a book to the library."
    )
    @Action
    String returnBook(ReturnBookReceipt returnBookReceipt) {
        return String.format("Book %s returned by member %s. Late fee: $%.2f",
                returnBookReceipt.bookId(),
                returnBookReceipt.memberId(),
                returnBookReceipt.fee());
    }

    public record ReturnBookRequest(String bookId, String memberId) {}

    public record BorrowCard(String memberId, String bookId, Date dateBorrowed, Date dueDate, Date dateReturned) {}

    public record LateFee(double fee, long lateDays) {}

    public record ReturnBookReceipt(String memberId, String bookId, double fee) {}
}
