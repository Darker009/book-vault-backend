package org.tech.recommendation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KnnRecommender {

    private final List<BookFeatureVector> bookVectors;

    public KnnRecommender(List<BookFeatureVector> bookVectors) {
        this.bookVectors = bookVectors;
    }

    public List<Long> getTopKSimilarBooks(BookFeatureVector target, int k) {
        List<SimilarityResult> results = new ArrayList<>();
        for (BookFeatureVector vector : bookVectors) {
            if (!vector.getBookId().equals(target.getBookId())) {
                double distance = DistanceUtil.euclideanDistance(target.getFeatures(), vector.getFeatures());
                results.add(new SimilarityResult(vector.getBookId(), distance));
            }
        }
        return results.stream()
                .sorted(Comparator.comparingDouble(SimilarityResult::getDistance))
                .limit(k)
                .map(SimilarityResult::getBookId)
                .collect(Collectors.toList());
    }

    private static class SimilarityResult {
        private final Long bookId;
        private final double distance;

        public SimilarityResult(Long bookId, double distance) {
            this.bookId = bookId;
            this.distance = distance;
        }

        public Long getBookId() {
            return bookId;
        }

        public double getDistance() {
            return distance;
        }
    }
}
