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

    // 1) Parse initial user input.
    //    If we already got both IDs, this action can satisfy essentialsKnown.
    @Action(post = {"essentialsKnown", "essentialsMissing"})
    ReturnBookRequest processRequest(UserInput userInput, OperationContext ctx) {
        var prompt = """
                You are a helpful library assistant.
                A user wants to return a book.
                Extract the book ID and member ID from the user's input.
                If the information is not available, respond with "Unknown".
                
                User Input: %s
                """.formatted(userInput.getContent());
        return ctx.promptRunner().createObject(prompt, ReturnBookRequest.class);
    }

    @Action(pre = {"essentialsMissing"}, post = {"essentialsKnown"})
    ReturnBookRequest requestEssentials(ReturnBookRequest req, UserInput userInput, OperationContext ctx) {
        System.out.println("-------------------------------------------- Mocked requestEssentials called");
        return new ReturnBookRequest("321-XYZ", "M-789");
    }

    // 3) Preconditions (computed independently).
    @Condition
    public boolean essentialsKnown(ReturnBookRequest req) {

        return req != null
                && req.bookId() != null && !req.bookId().isBlank() && !"Unknown".equalsIgnoreCase(req.bookId())
                && req.memberId() != null && !req.memberId().isBlank() && !"Unknown".equalsIgnoreCase(req.memberId());
    }

    @Condition
    public boolean essentialsMissing(ReturnBookRequest req) {
        // IMPORTANT: compute directly; do NOT delegate to essentialsKnown(req)
        System.out.println("-------------------------------------------- essentialsMissing: " + req);
        return req == null
                || req.bookId() == null || req.bookId().isBlank() || "Unknown".equalsIgnoreCase(req.bookId())
                || req.memberId() == null || req.memberId().isBlank() || "Unknown".equalsIgnoreCase(req.memberId());
    }

    // 4) Gated on essentialsKnown.
    @Action(pre = {"essentialsKnown"})
    BorrowCard retrieveBorrowCard(ReturnBookRequest req) {
        // Note: record signature is (memberId, bookId, ...)
        return new BorrowCard(
                req.memberId(),
                req.bookId(),
                new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000), // borrowed 7 days ago
                new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000), // due in 7 days (demo)
                new Date(System.currentTimeMillis())                             // returned now
        );
    }

    @Action
    LateFee processLateFee(BorrowCard borrowCard) {
        long millisDiff = new Date().getTime() - borrowCard.dueDate().getTime();
        long lateDays = millisDiff > 0 ? (millisDiff / (1000 * 60 * 60 * 24)) : 0L;
        double fee = lateDays; // $1 per late day
        return new LateFee(fee, lateDays);
    }

    @Action
    ReturnBookReceipt generateReceipt(BorrowCard borrowCard, LateFee lateFee) {
        return new ReturnBookReceipt(borrowCard.memberId(), borrowCard.bookId(), lateFee.fee());
    }

    @AchievesGoal(description = "Handles the return of a book to the library.")
    @Action
    String returnBook(ReturnBookReceipt receipt) {
        return String.format("Book %s returned by member %s. Late fee: $%.2f",
                receipt.bookId(), receipt.memberId(), receipt.fee());
    }

    // ===== Records =====
    public record ReturnBookRequest(String bookId, String memberId) {}

    public record BorrowCard(String memberId, String bookId, Date dateBorrowed, Date dueDate, Date dateReturned) {}

    public record LateFee(double fee, long lateDays) {}

    public record ReturnBookReceipt(String memberId, String bookId, double fee) {}
}
