package learning.kirana_stores.controller;

import learning.kirana_stores.entities.Post;
import learning.kirana_stores.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report")
public class TransactionReport {

    private final ReportService reportService;

    /**
     *
     * @param reportService
     */
    @Autowired
    public TransactionReport(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     *
     * @return
     */
    @GetMapping("/weekly")
    public List<Post> generateWeeklyReports() {
        // Add your code here to calculate total credit, total debit, and net flow
        List<Post> weeklyReports = reportService.generateWeeklyReports();
        calculateTotals(weeklyReports);
        return weeklyReports;
    }

    @GetMapping("/monthly")
    public List<Post> generateMonthlyReports() {
        // Add your code here to calculate total credit, total debit, and net flow
        List<Post> monthlyReports = reportService.generateMonthlyReports();
        calculateTotals(monthlyReports);
        return monthlyReports;
    }

    @GetMapping("/yearly")
    public List<Post> generateYearlyReports() {
        // Add your code here to calculate total credit, total debit, and net flow
        List<Post> yearlyReports = reportService.generateYearlyReports();
        calculateTotals(yearlyReports);
        return yearlyReports;
    }

    private void calculateTotals(List<Post> reports) {
        double totalCredit = 0.0;
        double totalDebit = 0.0;

        for (Post report : reports) {
            if ("CREDIT".equals(report.getTransactionType())) {
                totalCredit += report.getAmount().doubleValue(); // Convert BigDecimal to double
            } else if ("DEBIT".equals(report.getTransactionType())) {
                totalDebit += report.getAmount().doubleValue(); // Convert BigDecimal to double
            }
        }

        double netFlow = totalCredit - totalDebit;

        for (Post report : reports) {
            report.setTotalCredit(totalCredit);
            report.setTotalDebit(totalDebit);
            report.setNetFlow(netFlow);
        }
    }

}
