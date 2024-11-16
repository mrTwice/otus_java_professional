package ru.otus.java.professional.yampolskiy.patterns.two.service;

import ru.otus.java.professional.yampolskiy.patterns.two.transactional.Transactional;

public interface Service {
    @Transactional
    void fillData();
    @Transactional
    void updatePrice();
   @Transactional
    void clearDatabase();
}
