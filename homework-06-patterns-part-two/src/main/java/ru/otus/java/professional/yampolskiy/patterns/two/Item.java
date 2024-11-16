package ru.otus.java.professional.yampolskiy.patterns.two;

public class Item {
    private long id;
    private String Tittle;
    private double price;

    public Item() {
    }

    public Item(long id, String tittle, double price) {
        this.id = id;
        Tittle = tittle;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTittle() {
        return Tittle;
    }

    public void setTittle(String tittle) {
        Tittle = tittle;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
