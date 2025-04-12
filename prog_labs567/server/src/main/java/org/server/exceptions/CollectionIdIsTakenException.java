package org.server.exceptions;

/**
 * Исключение занятого айди коллекии. Выбрасывается при попытке добавления элемента с уже существующим айди.
 */
public class CollectionIdIsTakenException extends Exception{
    public CollectionIdIsTakenException(){
        super("элемент с таким айди уже существует( попробуйте еще раз!");
    }
}
