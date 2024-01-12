package learning.kirana_stores.model.report;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
public class Report {
    @Id
    private String period;
    private BigDecimal totalCredit;
    private BigDecimal totalDebit;
    private BigDecimal netFlow;
}
