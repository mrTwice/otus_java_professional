package ru.otus.java.professional.yampolskiy.streamapi;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final TaskFactory taskFactory = new TaskFactory();
    private static final Random random = new Random();

    public static void main(String[] args) {
        List<Task> tasks = taskFactory.getTasks(10);
        List<Task> inProgress = tasks.stream()
                .filter(task -> TaskStatus.IN_PROGRESS.equals(task.getStatus()))
                .toList();
        inProgress.forEach(System.out::println);

        Long closedTasksCount = tasks.stream()
                .filter(task -> task.getStatus().equals(TaskStatus.CLOSED))
                .count();

        Long existId = getExistId(tasks);
        boolean isExist = tasks.stream()
                .anyMatch(task -> existId.equals(task.getId()));
        System.out.printf("Есть ли в списке задача с id = %s: %b\n", existId, isExist);

        Long notExistId = getNotExistId(tasks);
        boolean notExist = tasks.stream()
                .noneMatch(task -> task.getId().equals(notExistId));
        System.out.printf("Отсутствует ли в списке задача с id = %s: %b\n", notExistId, notExist);

        List<Task> sorted = tasks.stream()
                .sorted(Comparator.comparing(Task::getStatus))
                .toList();
        sorted.forEach(System.out::println);

        Map<TaskStatus, Map<String, List<Task>>> groupedTasks = tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus,
                        Collectors.groupingBy(task -> task.getId() % 2 == 0 ? "Четные ID" : "Нечетные ID")));

        groupedTasks.forEach((status, groupedById) -> {
            System.out.println("Статус: " + status);
            groupedById.forEach((idType, taskList) -> {
                System.out.println("  " + idType + ":");
                taskList.forEach(System.out::println);
            });
        });

        Map<Boolean, List<Task>> partitionedTasks = tasks.stream()
                .collect(Collectors.partitioningBy(task -> task.getStatus().equals(TaskStatus.CLOSED)));

        partitionedTasks.forEach((isClosed, taskList) -> {
            String status = isClosed ? "Закрытые задачи" : "Все остальные";
            System.out.println(status + ":");
            taskList.forEach(System.out::println);
        });
    }

    private static Long getExistId(List<Task> tasks) {
        return tasks.stream()
                .map(Task::getId)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Список задач пуст"));
    }

    private static Long getNotExistId(List<Task> tasks) {

        Set<Long> existingIds = tasks.stream()
                .map(Task::getId)
                .collect(Collectors.toSet());

        Long newId;
        do {
            newId = random.nextLong();
        } while (existingIds.contains(newId));

        return newId;
    }
}
