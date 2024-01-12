package learning.kirana_stores.entities;

import learning.kirana_stores.enums.TransactionType;
import learning.kirana_stores.model.DateAudit;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document (collection = "Kirana")
@ToString
public class Post extends DateAudit {
    private double totalCredit;
    private double totalDebit;
    private double netFlow;
    private String id;
    private int userId;
    private String currency;
    private double originalAmount;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime dateTime;
}
