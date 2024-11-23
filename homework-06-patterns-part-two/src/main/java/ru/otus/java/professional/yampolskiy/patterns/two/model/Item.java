package ru.otus.java.professional.yampolskiy.patterns.two.model;

public class Item {
    private Long id;
    private String tittle;
    private double price;

    public Item() {
    }

    public Item(Long id, String tittle, double price) {
        this.id = id;
        this.tittle = tittle;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
