package ru.otus.java.professional.yampolskiy.patterns.two;

import ru.otus.java.professional.yampolskiy.patterns.two.service.Service;

public class Main {
    public static void main(String[] args) {

        Service itemsServiceProxy = ItemsServiceProxy.create();
        itemsServiceProxy.clearDatabase();
        itemsServiceProxy.fillData();
        itemsServiceProxy.updatePrice();
    }
}
