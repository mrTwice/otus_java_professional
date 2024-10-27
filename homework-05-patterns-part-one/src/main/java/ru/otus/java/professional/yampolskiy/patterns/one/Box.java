package ru.otus.java.professional.yampolskiy.patterns.one;

import java.awt.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.awt.Color.*;

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
    public SmallFirstIterator getSmallFirstIterator(){
        return new SmallFirstIterator();
    }

    public ColorFirstIterator getColorFirstIterator(){
        return new ColorFirstIterator();
    }

    public class SmallFirstIterator implements Iterator<String> {

        int cursorRed;
        int cursorBlue;
        int cursorGreen;
        int cursorMagenta;
        Color nextColor = Color.RED;

        @Override
        public boolean hasNext() {
            return cursorRed < red.getItems().size() ||
                    cursorBlue < blue.getItems().size() ||
                    cursorGreen < green.getItems().size() ||
                    cursorMagenta < magenta.getItems().size();
        }

        @Override
        public String next() {
            while (true) {
                if (nextColor.equals(Color.RED) && cursorRed < red.getItems().size()) {
                    nextColor = BLUE;
                    return getElementAndIncrementCursor(red.getColorName(), cursorRed++, red);
                } else if (nextColor.equals(BLUE) && cursorBlue < blue.getItems().size()) {
                    nextColor = GREEN;
                    return getElementAndIncrementCursor(blue.getColorName(), cursorBlue++, blue);
                } else if (nextColor.equals(GREEN) && cursorGreen < green.getItems().size()) {
                    nextColor = MAGENTA;
                    return getElementAndIncrementCursor(green.getColorName(), cursorGreen++, green);
                } else if (nextColor.equals(MAGENTA) && cursorMagenta < magenta.getItems().size()) {
                    nextColor = Color.RED;
                    return getElementAndIncrementCursor(magenta.getColorName(), cursorMagenta++, magenta);
                }
                rotateNextColor();
                if (!hasNext()) throw new NoSuchElementException("Элементов удовлетворяющих условиям нет");
            }
        }

        private void rotateNextColor() {
            if (nextColor.equals(Color.RED)) {
                nextColor = Color.BLUE;
            } else if (nextColor.equals(Color.BLUE)) {
                nextColor = Color.GREEN;
            } else if (nextColor.equals(Color.GREEN)) {
                nextColor = Color.MAGENTA;
            } else if (nextColor.equals(Color.MAGENTA)) {
                nextColor = Color.RED;
            } else {
                throw new RuntimeException("Неизвестный цвет");
            }
        }

        private String getElementAndIncrementCursor(String colorName, int index, T matryoshka) {
            return colorName + matryoshka.getItems().get(index);
        }
    }

    public class ColorFirstIterator implements Iterator<String>{

        int cursorRed;
        int cursorBlue;
        int cursorGreen;
        int cursorMagenta;
        boolean redIsAvailable;
        boolean blueIsAvailable;
        boolean greenIsAvailable;
        boolean magentaIsAvailable;

        @Override
        public boolean hasNext() {
            redIsAvailable = cursorRed < red.getItems().size();
            blueIsAvailable = cursorBlue < blue.getItems().size();
            greenIsAvailable = cursorGreen < green.getItems().size();
            magentaIsAvailable = cursorMagenta < magenta.getItems().size();
            return redIsAvailable || blueIsAvailable || greenIsAvailable || magentaIsAvailable;
        }

        @Override
        public String next() {
            if (redIsAvailable) {
                return getElementAndIncrementCursor(red.getColorName(), cursorRed++, red);
            }
            if (blueIsAvailable) {
                return getElementAndIncrementCursor(blue.getColorName(), cursorBlue++, blue);
            }
            if (greenIsAvailable) {
                return getElementAndIncrementCursor(green.getColorName(), cursorGreen++, green);
            }
            if (magentaIsAvailable) {
                return getElementAndIncrementCursor(magenta.getColorName(), cursorMagenta++, magenta);
            }
            throw new NoSuchElementException("Элементов удовлетворяющих условиям нет");
        }

        private String getElementAndIncrementCursor(String colorName, int index, T matryoshka) {
            return colorName + matryoshka.getItems().get(index);
        }
    }

}
