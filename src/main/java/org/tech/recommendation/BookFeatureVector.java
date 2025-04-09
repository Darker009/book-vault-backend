package org.tech.recommendation;

public class BookFeatureVector {

    private Long bookId;
    private double[] features;

    public BookFeatureVector(Long bookId, double[] features) {
        this.bookId = bookId;
        this.features = features;
    }

    public Long getBookId() {
        return bookId;
    }

    public double[] getFeatures() {
        return features;
    }
}
