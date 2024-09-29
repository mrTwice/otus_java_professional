package ru.otus.java.professional.yampolskiy.streamapi;

public enum TaskStatus {

    OPEN("Открыта"),
    IN_PROGRESS("В работе"),
    CLOSED("Закрыта");

    private String description;

    TaskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

