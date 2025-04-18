## дисциплина "программирование"
### лабораторная работа №7

*многопоточное клиент-серверное приложение с взаимодействием с базой данных PostgreSQL*

для запуска:

    mvn package
    cd server/target
    java -jar server.jar <port> jdbc:postgresql://localhost:5432/<db_name> <user> <password>
    cd ../../client/target
    java -jar client.jar <host> <port>


__задание:__
* Организовать хранение коллекции в реляционной СУБД (PostgresQL). Убрать хранение коллекции в файле.
* Для генерации поля id использовать средства базы данных (sequence).
* Обновлять состояние коллекции в памяти только при успешном добавлении объекта в БД
* Все команды получения данных должны работать с коллекцией в памяти, а не в БД
* Организовать возможность регистрации и авторизации пользователей. У пользователя есть возможность указать пароль.
* Пароли при хранении хэшировать алгоритмом MD2
* Запретить выполнение команд не авторизованным пользователям.
* При хранении объектов сохранять информацию о пользователе, который создал этот объект.
* Пользователи должны иметь возможность просмотра всех объектов коллекции, но модифицировать могут только принадлежащие им.
* Для идентификации пользователя отправлять логин и пароль с каждым запросом.
* Необходимо реализовать многопоточную обработку запросов.


* _Для многопоточного чтения запросов использовать **Fixed thread pool**_
* _Для многопотчной обработки полученного запроса использовать **Fixed thread pool**_
* _Для многопоточной отправки ответа использовать **создание нового потока (java.lang.Thread)**_
* _Для синхронизации доступа к коллекции использовать синхронизацию чтения и записи с помощью **synchronized**_