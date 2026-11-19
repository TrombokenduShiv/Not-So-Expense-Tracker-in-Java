import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Binary-name compatibility shim for the original default-package nested class. */
public final class ExpenseTracker {
    private ExpenseTracker() { }
    public static final class Transaction implements Serializable {
        private static final long serialVersionUID = 1L;
        final int id;
        final LocalDate date;
        final String type;
        final String category;
        final BigDecimal amount;
        final String description;
        public Transaction(int id, LocalDate date, String type, String category,
                           BigDecimal amount, String description) {
            this.id=id; this.date=date; this.type=type; this.category=category;
            this.amount=amount; this.description=description;
        }
    }
}
