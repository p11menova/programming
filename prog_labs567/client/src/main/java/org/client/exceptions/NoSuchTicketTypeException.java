package org.client.exceptions;

/**
 * Исключение отсутствия типа билета. Выбрасывается при попытке обращения к несуществующему типу билета.
 */
public class NoSuchTicketTypeException extends Exception{
    public NoSuchTicketTypeException(){
        super("нет такого типа билета(\nвозможные варианты:");

    }
}
