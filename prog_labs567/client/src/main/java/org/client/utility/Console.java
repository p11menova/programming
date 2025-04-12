package org.client.utility;

import org.common.models.DBModels.UserData;
import org.common.models.Ticket;
import org.common.utility.Printable;
import org.client.utility.ModelsAskers.NewTicketAsker;
import org.client.exceptions.NoSuchElementException;
import org.common.interaction.Request;

import org.client.exceptions.WrongFileRightException;


import java.io.FileNotFoundException;
import java.util.EmptyStackException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс Консоли - управление пользовательским вводом/вводом из файла
 */
public class Console implements Printable {
    /**
     * Текущий сканнер
     */
    public Scanner in; // текущий сканнер
    /**
     * Сканнер из скрипта
     */
    public Scanner script_in; // сканнер из скрипта
    public String promt1 = "$ ";
    public String promt2 = " ?>";

    public ConsoleMode consoleMode = ConsoleMode.INTERACTIVE;
    public boolean notPrinting = false; // не печатает при создании нового экземпляра тикет
    public Request request;
    public boolean requestIsReady = false;
    public UserData userData;

    public boolean user_in = false;


    public Console(Scanner scanner) {
        this.in = scanner;
    }

    /**
     * Печатает объект в поток стандартного вывода с новой строки
     *
     * @param obj объект для печати
     */
    @Override
    public void println(Object obj) {
        if (!notPrinting) System.out.println(obj);

    }

    /**
     * Печатает объект в поток стандартного вывода
     *
     * @param obj объект для печати
     */
    public void printf(Object obj) {
        if (!notPrinting) System.out.printf((String) obj);
    }

    /**
     * Печатает ошибку в поток стандартного вывода с новой строки
     *
     * @param obj ошибка для печати
     */
    @Override
    public void print_error(Object obj) {
        if (!notPrinting) System.out.println("error: " + obj);
    }

    /**
     * Печатает объект в поток стандартного вывода с табуляцией с новой строки
     *
     * @param obj объект для печати
     */
    public void println_with_t(Object obj) {
        if (!notPrinting) System.out.println("\t" + obj);
    }

    /**
     * Печатает объект в поток стандартного вывода с табуляцией
     *
     * @param obj объект для печати
     */
    public void printf_with_t(Object obj) {
        if (!notPrinting) System.out.printf("\t" + obj);
    }


    /**
     * Устанавливает сканнер скрипта
     *
     * @param in сканнер скрипта
     */
    public void setScriptScanner(Scanner in) {
        script_in = in;
    }

    /**
     * Устанавливает режим чтения из скрипта
     */
    public void setScriptMode(String filename) throws FileNotFoundException, WrongFileRightException, RuntimeException {
        if (ScriptRunManager.contains(filename))
            throw new RuntimeException("обнаружена рекурсия! завершение работы программы..".toUpperCase());
        ScriptRunManager.openFileScanner(filename);
        // this.script_in = ScriptRunManager.getScriptScanner();
        this.consoleMode = ConsoleMode.SCRIPT;
    }

    /**
     * Отменяет режим чтения из файла. Возврат к чтению стандартного потока ввода
     */
    public void denyFileMode() {
        consoleMode = ConsoleMode.INTERACTIVE;
        this.in = new Scanner(System.in);
    }

    /**
     * Обрабатывает чтение из скрипта
     *
     * @param //in       сканнер скрипта
     * @param //filename название исполняемого скрипта
     */
    public void readScriptInput() {
        try {
            this.script_in = ScriptRunManager.getScriptScanner();
            if (script_in.hasNextLine()) {
                String in_data = script_in.nextLine().trim() + " ";
                if (!in_data.equals(" ")) {
                    String[] in_data_splited = in_data.split(" ", 2);
                    String currentCommandName = in_data_splited[0];
                    String currentCommandArgs = in_data_splited[1];
                    this.request = new Request(currentCommandName, currentCommandArgs);
                } else {
                    this.request = new Request("", "");
                }
                this.requestIsReady = true;
            }
        } catch (NoSuchElementException | EmptyStackException e) {
            denyFileMode();
        }
    }

    public void readUserInput() {
        printf(promt1);
        if (in.hasNextLine()) {
            String in_data = this.in.nextLine().trim() + " ";
            if (in_data.equals(" ")) {
                println("пустой ввод :(( для получения информации о возможных командах введите 'help'");
            } else {
                String[] in_data_splited = in_data.split(" ", 2);
                String currentCommandName = in_data_splited[0];
                String currentCommandArgs = in_data_splited[1];
                this.request = new Request(currentCommandName, currentCommandArgs);
                this.requestIsReady = true;
            }
        }
    }

    public void readUserAuth() {
        String login = "";
        String password = "";
        printf("у вас уже есть аккаунт или вы впервые?\n1 - войти\n2 - зарегистрироваться\n");

        String loginOrReg = "";
        while ((loginOrReg == "") || !loginOrReg.equals("1") && !loginOrReg.equals("2")) {
            printf(promt1 + "введите 1 или 2:");
            if (in.hasNextLine()) loginOrReg = in.nextLine().trim();
        }


        while (Objects.equals(login, "")) {
            printf(promt1 + "введите логин:");
            if (in.hasNextLine()) {
                login = in.nextLine().trim();
            }
        }
        ;
        while (Objects.equals(password, "")) {
            printf(promt1 + "введите пароль:");
            if (in.hasNextLine()) {
                password = in.nextLine().trim();
            }
        }
        this.userData = new UserData(loginOrReg, login, password);
        this.user_in = true;
    }

    public UserData getUserData() {
        boolean user_in = false;
        while (!user_in) {
            readUserAuth();
            return this.userData;
        }
        return null;
    }

    public Request getRequest() {
        while (!this.requestIsReady)
            switch (consoleMode) {
                case INTERACTIVE -> readUserInput();
                case SCRIPT -> readScriptInput();
            }
        this.requestIsReady = false;
        return this.request;
    }

    public Ticket makeNewTicket(int id) {
        if (consoleMode == ConsoleMode.INTERACTIVE)
            return new NewTicketAsker(this).validateTicketFromInteractiveMode(id);
        notPrinting = true;
        Ticket newElem = new NewTicketAsker(this).validateTicketFromScript(id);
        notPrinting = false;
        return newElem;
    }

}