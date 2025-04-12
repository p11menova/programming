package org.client.exceptions;

/**
 * Исключение отсутствия команды. Выбрасывается при попытке выполнить несуществующую команду.
 */
public class NoSuchCommandException extends Exception{
    public NoSuchCommandException(){
        super("нет такой команды ;( \nдля получения списка команд введите 'help'");
    }
}
