package learning.kirana_stores.services;

import learning.kirana_stores.entities.Post;
import learning.kirana_stores.repository.TransactionsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReportService {

    private final TransactionsRepo transactionsRepo;

    @Autowired
    public ReportService(TransactionsRepo transactionsRepo) {
        this.transactionsRepo = transactionsRepo;
    }

    public List<Post> generateWeeklyReports() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minus(7, ChronoUnit.DAYS);

        return transactionsRepo.getTransactionBetweenDates(startDate, endDate);
    }

    public List<Post> generateMonthlyReports() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minus(30, ChronoUnit.DAYS);

        return transactionsRepo.getTransactionBetweenDates(startDate, endDate);
    }

    public List<Post> generateYearlyReports() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minus(365, ChronoUnit.DAYS);

        return transactionsRepo.getTransactionBetweenDates(startDate, endDate);
    }

    public List<Post> getTransactionsBetweenDates(String start, String end) {
        LocalDateTime startDate = convertStringToLocalDateTime(start);
        LocalDateTime endDate = convertStringToLocalDateTime(end);

        return transactionsRepo.getTransactionBetweenDates(startDate, endDate);
    }

    private LocalDateTime convertStringToLocalDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate.atStartOfDay();
    }
}
