package ru.otus.java.professional.yampolskiy.streamapi;

public class Task {
    private Long id;
    private String title;
    private TaskStatus status;

    public Task(Long id, String title, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " TITLE: " + title +
                " STATUS: " + status;
    }
}
