package ru.otus.java.professional.yampolskiy.homework.reflection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.METHOD})
public @interface Disabled {
    String message() default "";
}
