# Домашнее задание
## Тема: Применение Java Persistence API
### Цель: Научиться работать с Hibernate

#### Описание:
* В базе данных необходимо реализовать возможность хранить информацию о:
  * покупателях (id, имя)
  * товарах (id, название, стоимость).
* У каждого покупателя свой набор купленных товаров.
* Необходимо написать консольное приложение, которое позволит:
  * посмотреть, какие товары покупал клиент,
  * посмотреть какие клиенты купили определенный товар,
  * удалить из базы товары/покупателей.
  * (*) Добавить детализацию по паре «покупатель — товар»: сколько стоил товар в момент покупки клиентом.