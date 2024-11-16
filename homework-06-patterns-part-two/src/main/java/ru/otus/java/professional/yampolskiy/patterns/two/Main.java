package ru.otus.java.professional.yampolskiy.patterns.two;

public class Main {
    private static final ItemsService itemsService = ItemsService.getInstance();
    public static void main(String[] args) {
        itemsService.clearDatabase();
        itemsService.fillData();
        itemsService.updatePrice();
    }
}
