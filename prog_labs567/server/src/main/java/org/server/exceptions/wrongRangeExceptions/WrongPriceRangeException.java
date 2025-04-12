package org.server.exceptions.wrongRangeExceptions;

/**
 * Исключение неверного значения поля price. Выбрасывается при попытке задать полю price отрицательное значение.
 */
public class WrongPriceRangeException extends Exception{
    public WrongPriceRangeException(){
        super("Поле price должно быть >0");
    }
}
