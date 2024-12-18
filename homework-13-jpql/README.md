# Домашнее задание
## Тема: Использование Hibernate
### Цель: На практике освоить основы Hibernate. Понять как аннотации-hibernate влияют на формирование sql-запросов.

**Описание**: Создайте класс Client поля:

**Создать классы:**
  * `Client`
    ```java
    public class Client{
        private Long id;
        private String name;
        private Address address;
        private Phone phone;
      }
      ```
  
  * `Address`
    ```java
    public class Address {
        private Long id;
        private String street;
    }
    ```
  
  * `Phone`
    ```java
    public class Phone {
        private Long id;
        private Client client;
        private String number;
    }
    ```
* Установить связь между сущностями:
  * Clietn и Address - OneToOne
  * Client и Phone - ManyToMany

### ВАЖНО:

* Разметить классы чтобы:
  * при сохранении/чтении объека Client каскадно сохранялись/читались вложенные объекты.
* Hibernate должен создать только три таблицы:
  * для телефонов
  * адресов
  * клиентов. 
* При сохранении нового объекта не должно быть update-ов. 
  * Посмотреть логи и проверить, что эти два требования выполняются.