package ru.otus.java.professional.yampolskiy.patterns.one;


import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Box<T extends Matryoshka<Color>> {
    private final T red;
    private final T blue;
    private final T green;
    private final T magenta;

    public Box(T red, T blue, T green, T magenta) {
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.magenta = magenta;
    }

    public SmallFirstIterator getSmallFirstIterator() {
        return new SmallFirstIterator();
    }

    public ColorFirstIterator getColorFirstIterator() {
        return new ColorFirstIterator();
    }

    public class SmallFirstIterator implements Iterator<String> {
        private final List<T> matryoshkas = List.of(red, blue, green, magenta);
        private final int[] cursors = new int[matryoshkas.size()];
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            for (int i = 0; i < matryoshkas.size(); i++) {
                if (cursors[i] < matryoshkas.get(i).getItems().size()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String next() {
            for (int i = 0; i < matryoshkas.size(); i++) {
                int index = (currentIndex + i) % matryoshkas.size();
                T matryoshka = matryoshkas.get(index);
                int cursor = cursors[index];

                if (cursor < matryoshka.getItems().size()) {
                    cursors[index]++;
                    currentIndex = (index + 1) % matryoshkas.size();
                    return getElementAndIncrementCursor(matryoshka.getColorName(), cursor, matryoshka);
                }
            }
            throw new NoSuchElementException("Элементов удовлетворяющих условиям нет");
        }


    }

    public class ColorFirstIterator implements Iterator<String> {

        private final List<T> matryoshkas = List.of(red, blue, green, magenta); // Список всех матрёшек
        private final int[] cursors = new int[matryoshkas.size()]; // Курсоры для каждой матрёшки

        @Override
        public boolean hasNext() {
            for (int i = 0; i < matryoshkas.size(); i++) {
                if (cursors[i] < matryoshkas.get(i).getItems().size()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String next() {
            for (int i = 0; i < matryoshkas.size(); i++) {
                if (cursors[i] < matryoshkas.get(i).getItems().size()) {
                    T matryoshka = matryoshkas.get(i);
                    int cursor = cursors[i]++;
                    return getElementAndIncrementCursor(matryoshka.getColorName(), cursor, matryoshka);
                }
            }
            throw new NoSuchElementException("Элементов удовлетворяющих условиям нет");
        }

    }

    protected String getElementAndIncrementCursor(String colorName, int index, T matryoshka) {
        return colorName + matryoshka.getItems().get(index);
    }

}
