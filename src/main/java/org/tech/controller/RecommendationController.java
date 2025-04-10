package org.tech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tech.entity.Book;
import org.tech.service.RecommendationService;
import org.tech.service.BorrowService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "http://localhost:5173") // ✅ allow frontend calls
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private BorrowService borrowService;

    // ✅ 1. Get similar books using KNN
    @GetMapping("/{bookId}")
    public ResponseEntity<List<Book>> getRecommendations(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "5") int k
    ) {
        List<Book> recommendedBooks = recommendationService.getRecommendedBooks(bookId, k);
        return ResponseEntity.ok(recommendedBooks);
    }

    // ✅ 2. Summary of recommendations for admin dashboard
    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getRecommendationSummary() {
        return ResponseEntity.ok(recommendationService.getSummary());
    }

    // ✅ 3. Borrow stats: books borrowed per student
    @GetMapping("/admin/borrow-stats")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Long>> getBorrowStats() {
        return ResponseEntity.ok(borrowService.getBorrowStatsByStudent());
    }
}
