package org.tech.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tech.service.BookService;
import org.tech.service.BorrowService;
import org.tech.service.StatsService;
import org.tech.service.UserService;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private BorrowService borrowService;

    @GetMapping("/books")
    public ResponseEntity<Map<String, Object>> getBookStats() {
        return ResponseEntity.ok(statsService.getBookStats());
    }

    @GetMapping("/recommendations")
    public ResponseEntity<Map<String, Object>> getRecommendationStats() {
        return ResponseEntity.ok(statsService.getRecommendationStats());
    }

    @GetMapping("/borrows")
    public ResponseEntity<Map<String, Long>> getBorrowStatsByStudent() {
        return ResponseEntity.ok(statsService.getBorrowStatsByStudent());
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummaryStats() {
        System.out.println("⚡ /stats/summary called!");
        return ResponseEntity.ok(statsService.getSummaryStats());
    }


}
