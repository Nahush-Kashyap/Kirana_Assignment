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

    /**
     * Generates weekly reports based on transactions within the last 7 days.
     * @return List of weekly reports.
     */
    public List<Post> generateWeeklyReports() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minus(7, ChronoUnit.DAYS);

        return transactionsRepo.getTransactionBetweenDates(startDate, endDate);
    }

    /**
     * Generates monthly reports based on transactions within the last 30 days.
     * @return List of monthly reports.
     */
    public List<Post> generateMonthlyReports() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minus(30, ChronoUnit.DAYS);

        return transactionsRepo.getTransactionBetweenDates(startDate, endDate);
    }

    /**
     * Generates yearly reports based on transactions within the last 365 days.
     * @return List of yearly reports.
     */
    public List<Post> generateYearlyReports() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minus(365, ChronoUnit.DAYS);

        return transactionsRepo.getTransactionBetweenDates(startDate, endDate);
    }

    /**
     * Retrieves transactions between specified start and end dates.
     * @param start The start date in "yyyy-MM-dd" format.
     * @param end The end date in "yyyy-MM-dd" format.
     * @return List of transactions between the specified dates.
     */
    public List<Post> getTransactionsBetweenDates(String start, String end) {
        LocalDateTime startDate = convertStringToLocalDateTime(start);
        LocalDateTime endDate = convertStringToLocalDateTime(end);

        return transactionsRepo.getTransactionBetweenDates(startDate, endDate);
    }

    /**
     * Converts a date string in "yyyy-MM-dd" format to LocalDateTime.
     * @param dateString The date string.
     * @return LocalDateTime representation of the date.
     */
    private LocalDateTime convertStringToLocalDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate.atStartOfDay();
    }
}
