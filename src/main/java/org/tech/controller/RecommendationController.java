package org.tech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tech.entity.Book;
import org.tech.service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "http://localhost:5173") // ✅ keep this for frontend access
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    // ✅ Get similar books using KNN
    @GetMapping("/{bookId}")
    public ResponseEntity<List<Book>> getRecommendations(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "5") int k
    ) {
        List<Book> recommendedBooks = recommendationService.getRecommendedBooks(bookId, k);
        return ResponseEntity.ok(recommendedBooks);
    }
}
