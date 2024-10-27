package ru.otus.java.professional.yampolskiy.patterns.one;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Matryoshka <T extends Color> {
    private final T color;
    private final List<String> items;

    public Matryoshka(int levels, T color){
        this.color = color;
        this.items = new ArrayList<>();
        for (int i = 0; i < levels; i++) {
            items.add(Integer.toString(i));
        }
    }

    public List<String> getItems() {
        return items;
    }

    public T getColor() {
        return color;
    }

    public String getColorName() {
        if (Color.RED.equals(color)) {
            return "Красный";
        } else if (Color.BLUE.equals(color)) {
            return "Синий";
        } else if (Color.GREEN.equals(color)) {
            return "Зелёный";
        } else if (Color.MAGENTA.equals(color)) {
            return "Малиновый";
        } else {
            return String.format("RGB(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
        }
    }

}
