package org.tech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service
public class StatsService {

    @Autowired
    private BookService bookService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private BorrowService borrowService;


    @Autowired
    private UserService userService;

    public Map<String, Object> getBookStats() {
        return bookService.getBookStatistics();
    }

    public Map<String, Object> getRecommendationStats() {
        return recommendationService.getSummary();
    }

    public Map<String, Long> getBorrowStatsByStudent() {
        return borrowService.getBorrowStatsByStudent();
    }

    public Map<String, Object> getSummaryStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBooks", bookService.getTotalBooks());
        stats.put("booksByGenre", bookService.getBooksByGenre());
        stats.put("totalStudents", userService.getTotalStudentCount());
        stats.put("borrowStats", borrowService.getBorrowSummary());
        return stats;
    }
}
