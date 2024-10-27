package ru.otus.java.professional.yampolskiy.patterns.one;

import java.awt.*;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        Product product = Product.builder()
                .id(10)
                .title("SomeTitle")
                .description("SomeDescription")
                .length(1.1)
                .weight(2.2)
                .width(3.3)
                .height(4.4)
                .build();


        Box<Matryoshka<Color>> box = new Box<>(
                new Matryoshka<>(3, Color.RED),
                new Matryoshka<>(3, Color.BLUE),
                new Matryoshka<>(3, Color.GREEN),
                new Matryoshka<>(3, Color.MAGENTA)
        );

        Iterator<String> iterator = box.getSmallFirstIterator();

        while (iterator.hasNext()){
            System.out.print(iterator.next() + " ");
        }

        System.out.println();
        System.out.println();

        Iterator<String> iterator1 = box.getColorFirstIterator();
        while (iterator1.hasNext()){
            System.out.print(iterator1.next() + " ");
        }
    }


}
