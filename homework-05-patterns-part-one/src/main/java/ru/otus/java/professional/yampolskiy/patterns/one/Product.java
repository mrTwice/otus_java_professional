package ru.otus.java.professional.yampolskiy.patterns.one;

public class Product {
    private final long id;
    private final String title;
    private final String description;
    private final double weight;
    private final double width;
    private final double length;
    private final double height;

    public static Builder builder(){
        return new Builder();
    }

    private Product(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.weight = builder.weight;
        this.width = builder.width;
        this.length = builder.length;
        this.height = builder.height;
    }

    public static final class Builder {
        private long id;
        private String title;
        private String description;
        private double weight;
        private double width;
        private double length;
        private double height;

        private Builder(){}

        public Product build() {
            return new Product(this);
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder weight(double weight) {
            this.weight = weight;
            return this;
        }

        public Builder width(double width) {
            this.width = width;
            return this;
        }

        public Builder length(double length) {
            this.length = length;
            return this;
        }

        public Builder height(double height) {
            this.height = height;
            return this;
        }
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getWeight() {
        return weight;
    }

    public double getWidth() {
        return width;
    }

    public double getLength() {
        return length;
    }

    public double getHeight() {
        return height;
    }
}
