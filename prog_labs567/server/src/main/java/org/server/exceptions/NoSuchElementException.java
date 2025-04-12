package org.server.exceptions;

/**
 * Исключение отсутствия элемента коллекции. Выбрасывается при попытке обращения к несуществующему элементу коллекции.
 */
public class NoSuchElementException extends Exception{
    public NoSuchElementException(){
        super("элемента с таким айди не существует(");
    }
}
