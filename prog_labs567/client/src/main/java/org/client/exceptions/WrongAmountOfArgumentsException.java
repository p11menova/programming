package org.client.exceptions;

/**
 * Исключение неверного количества аргументов команды. Выбрасывается при попытке указания неверного числа аргументов команде.
 */
public class WrongAmountOfArgumentsException extends Exception{
    public WrongAmountOfArgumentsException(){
        super("неправильное число аргументов команды.");
    }

}
