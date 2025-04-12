package org.client;


import org.client.exceptions.WrongAmountOfArgumentsException;

import org.client.utility.*;

import java.util.Scanner;

/**
 * Главный класс запуска программы.
 */
public class Main {
    public static int MAX_RECONNECTION_ATTEMPTS = 5;
    public static int RECONNECTION_TIMEOUT = 5 * 1000;
    private static boolean initializeConnectionAddress(String[] args){
        try {
            if (args.length != 2) throw new WrongAmountOfArgumentsException();
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            if (port <=0 || port >= 65536) throw new IllegalArgumentException("неверное значение порта. использование host="+host+" port="+port);
            return true;
        } catch (NumberFormatException e){
            System.out.println(("порт должен быть целым положительным числом"));
        } catch (IllegalArgumentException | WrongAmountOfArgumentsException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    /**
     * Начинает выполнение менеджера запуска программы.
     * @param args
     */
    public static void main(String[] args) {
        if (!initializeConnectionAddress(args)) {
            System.out.println("подключение не может быть установлено.\nЗавершение работы программы");
            return;};
        Console console = new Console(new Scanner(System.in));
        Client client = new Client(args[0], Integer.parseInt(args[1]), console);
        client.go();
        // client.run();



    }
}


