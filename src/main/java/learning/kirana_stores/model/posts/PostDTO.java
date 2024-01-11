package learning.kirana_stores.model.posts;

import learning.kirana_stores.enums.TransactionType;
import learning.kirana_stores.model.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PostDTO extends DateAudit {
    private double originalAmount;
    private String currency;
    private TransactionType transactionType;
}
