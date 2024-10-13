package ru.otus.java.professional.yampolskiy.homework.reflection;

import ru.otus.java.professional.yampolskiy.homework.reflection.annotations.AfterSuite;
import ru.otus.java.professional.yampolskiy.homework.reflection.annotations.BeforeSuite;
import ru.otus.java.professional.yampolskiy.homework.reflection.annotations.Disabled;
import ru.otus.java.professional.yampolskiy.homework.reflection.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

import static ru.otus.java.professional.yampolskiy.homework.reflection.RunTests.TestStatisticKey.*;

public class RunTests {
    private static final Map<TestStatisticKey, Integer> statistics = new HashMap<>();

    static {
        statistics.put(TOTAL_TESTS, 0);
        statistics.put(TestStatisticKey.PASSED, 0);
        statistics.put(TestStatisticKey.FAILED, 0);
        statistics.put(TestStatisticKey.DISABLED, 0);
    }

    public static void execute() throws IOException, ClassNotFoundException {
        List<Class<?>> testClasses = scanDirectory("ru.otus.java.professional.yampolskiy.homework.reflection.tests");
        if (!testClasses.isEmpty())
            invokeClassesMethods(testClasses);
        else System.out.println("Классы с тестами не найдены.");
    }

    private static List<Class<?>> scanDirectory(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    Class<?> testClass = Class.forName(className);
                    if (validationTestClass(testClass) && !testClass.isAnnotationPresent(Disabled.class))
                        classes.add(testClass);
                }
            }
        }

        return classes;
    }

    private static boolean validationTestClass(Class<?> testClass) {
        if (testClass.isAnnotationPresent(Disabled.class)) {
            System.out.println("Класс: " + testClass.getSimpleName() + " отключен");
            System.out.println("Причина отключения: " + (testClass.getAnnotation(Disabled.class).message().isEmpty() ? "Не указана" : testClass.getAnnotation(Disabled.class).message()));
        }

        int beforeSuiteCount = 0;
        int afterSuiteCount = 0;
        boolean isTestClass = false;

        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class))
                beforeSuiteCount++;
            if (method.isAnnotationPresent(AfterSuite.class))
                afterSuiteCount++;

            boolean hasBeforeSuite = method.isAnnotationPresent(BeforeSuite.class);
            boolean hasAfterSuite = method.isAnnotationPresent(AfterSuite.class);
            boolean hasTest = method.isAnnotationPresent(Test.class);

            if (hasAfterSuite && hasBeforeSuite) {
                throw new RuntimeException("Метод " + method.getName() + " содержит конфликтующие аннотации: @BeforeSuite и @AfterSuite.");
            }

            if ((hasAfterSuite || hasBeforeSuite) && hasTest) {
                throw new RuntimeException("Метод " + method.getName() + " содержит конфликтующие аннотации: " +
                        ((hasBeforeSuite ? "@BeforeSuite" : "@AfterSuite")) + " и @Test.");
            }

            if (hasTest) {
                statistics.put(TOTAL_TESTS, statistics.get(TOTAL_TESTS) + 1);
                isTestClass = hasTest;
            }
        }

        if (beforeSuiteCount > 1)
            throw new RuntimeException("BeforeSuite не может быть использована больше одного раза");
        if (afterSuiteCount > 1)
            throw new RuntimeException("AfterSuite не может быть использована больше одного раза");

        return isTestClass;
    }

    private static void invokeClassesMethods(List<Class<?>> testClasses) {
        for (Class<?> testClass : testClasses) {
            List<Method> testMethods = new ArrayList<>();
            Method beforeSuite = null;
            Method afterSuite = null;

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(BeforeSuite.class))
                    beforeSuite = method;
                if (method.isAnnotationPresent(AfterSuite.class))
                    afterSuite = method;
                if (method.isAnnotationPresent(Test.class) && !method.isAnnotationPresent(Disabled.class)) {
                    testMethods.add(method);
                }
                if (method.isAnnotationPresent(Disabled.class)) {
                    System.out.println("Метод: " + method + " отключен");
                    System.out.println("Причина отключения: " + (method.getAnnotation(Disabled.class).message().isEmpty() ? "Не указана" : method.getAnnotation(Disabled.class).message()));
                    statistics.put(DISABLED, statistics.get(DISABLED) + 1);
                }
            }

            testMethods.sort(Comparator.comparingInt((Method method) -> method.getAnnotation(Test.class).value().getValue()).reversed());

            List<Method> orderedMethods = new ArrayList<>();

            if (beforeSuite != null)
                orderedMethods.add(beforeSuite);

            orderedMethods.addAll(testMethods);

            if (afterSuite != null)
                orderedMethods.add(afterSuite);

            for (Method method : orderedMethods) {
                try {
                    System.out.println();
                    method.invoke(null);
                    if(method.isAnnotationPresent(Test.class))
                        statistics.put(PASSED, statistics.get(PASSED) + 1);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    System.out.println("Метод упал с ошибкой: " + e);
                    statistics.put(FAILED, statistics.get(FAILED) + 1);
                }
            }

        }
    }

    public static void getStatistic(){
        System.out.println();
        System.out.println();
        System.out.println("Статистика:");
        for (TestStatisticKey key : TestStatisticKey.values()) {
            System.out.println(key.getDescription() + statistics.get(key));
        }
    }

    public enum TestStatisticKey {
        TOTAL_TESTS("Всего тестов: "),
        PASSED("Успешно пройдено: "),
        FAILED("Не прошло: "),
        DISABLED("Отключено: ");

        private final String description;

        TestStatisticKey(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

}
