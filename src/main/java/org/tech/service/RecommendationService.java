package org.tech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tech.entity.Book;
import org.tech.repository.BookRepository;
import org.tech.recommendation.BookFeatureVector;
import org.tech.recommendation.KnnRecommender;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private BookRepository bookRepository;

    // ✅ 1. Get recommended books using KNN
    public List<Book> getRecommendedBooks(Long bookId, int k) {
        List<Book> allBooks = bookRepository.findAll();

        // Convert each Book into a feature vector
        List<BookFeatureVector> featureVectors = allBooks.stream()
                .map(book -> new BookFeatureVector(book.getId(), book.toFeatureVector()))
                .collect(Collectors.toList());

        // Target book vector
        BookFeatureVector targetVector = featureVectors.stream()
                .filter(vector -> vector.getBookId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Target book not found"));

        // KNN
        KnnRecommender recommender = new KnnRecommender(featureVectors);
        List<Long> recommendedBookIds = recommender.getTopKSimilarBooks(targetVector, k);

        return allBooks.stream()
                .filter(book -> recommendedBookIds.contains(book.getId()) && !book.getId().equals(bookId))
                .collect(Collectors.toList());
    }

    // ✅ 2. Summary stats for Admin Dashboard
    public Map<String, Object> getSummary() {
        List<Book> allBooks = bookRepository.findAll();

        Map<String, Long> categoryCount = allBooks.stream()
                .collect(Collectors.groupingBy(
                        book -> Optional.ofNullable(book.getSection()).orElse("Unknown"),
                        Collectors.counting()
                ));

        Map<String, Long> genreCount = allBooks.stream()
                .collect(Collectors.groupingBy(
                        book -> Optional.ofNullable(book.getTags()).orElse("Uncategorized"),
                        Collectors.counting()
                ));

        long totalBooks = allBooks.size();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalBooks", totalBooks);
        summary.put("categoryCount", categoryCount);
        summary.put("genreCount", genreCount);

        return summary;
    }
}
