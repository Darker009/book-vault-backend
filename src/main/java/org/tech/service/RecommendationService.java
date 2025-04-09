package org.tech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tech.entity.Book;
import org.tech.repository.BookRepository;
import org.tech.recommendation.BookFeatureVector;
import org.tech.recommendation.KnnRecommender;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getRecommendedBooks(Long bookId, int k) {
        List<Book> allBooks = bookRepository.findAll();

        // Convert each Book into a feature vector.
        List<BookFeatureVector> featureVectors = allBooks.stream()
                .map(book -> new BookFeatureVector(book.getId(), book.toFeatureVector()))
                .collect(Collectors.toList());

        // Find the feature vector for the target book.
        BookFeatureVector targetVector = featureVectors.stream()
                .filter(vector -> vector.getBookId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Target book not found"));

        KnnRecommender recommender = new KnnRecommender(featureVectors);
        List<Long> recommendedBookIds = recommender.getTopKSimilarBooks(targetVector, k);

        return allBooks.stream()
                .filter(book -> recommendedBookIds.contains(book.getId()) && !book.getId().equals(bookId))
                .collect(Collectors.toList());
    }
}
