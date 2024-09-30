package ru.otus.java.professional.yampolskiy.homework.reflection.tests;

import ru.otus.java.professional.yampolskiy.homework.reflection.annotations.*;

// @Disabled
public class ClassForTests {

    @BeforeSuite
    public static void before() {
        System.out.printf("Выполнился метод: %s\n", "before()");
    }

    @AfterSuite
    public static void after() {
        System.out.printf("Вызов метода: %s\n", "after()");
    }

    @Test
    public static void firstTest(){
        System.out.printf("Вызов метода: %s\n", "firstTest()");
    }

    @Test
    public static void secondTest(){
        System.out.printf("Вызов метода: %s\n", "secondTest()");
    }

    @Test(Priority.FOUR)
    public static void thirdTest(){
        System.out.printf("Вызов метода: %s\n", "thirdTest()");
    }

    @Test(Priority.THREE)
    public static void fourTest(){
        System.out.printf("Вызов метода: %s\n", "fourTest()");
    }

    @Test(Priority.ONE)
    @Disabled(message = "Вызов метода")
    public static void fiveTest(){
        System.out.printf("Метод %s выполнятся не должен был\n", "fiveTest()");
    }


    @Test(Priority.TWO)
    public static void sixTest(){
        System.out.printf("Вызов метода: %s\n", "sixTest()");
        System.out.println(8/0);
    }
}
