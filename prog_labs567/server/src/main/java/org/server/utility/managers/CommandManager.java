package org.server.utility.managers;

import org.common.interaction.UserAuthStatus;
import org.common.models.DBModels.TicketWithMetadata;
import org.common.models.DBModels.UserData;
import org.server.App;
import org.server.commands.Command;
import org.server.commands.clientCommands.AbstractAddCommand;
import org.server.commands.clientCommands.ChangingCollectionCommand;

import org.server.commands.clientCommands.UserCommand;
import org.server.commands.serverCommands.AuthorizationCommand;
import org.server.exceptions.NoSuchCommandException;
import org.common.interaction.Request;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.server.utility.managers.RunManager.dbManager;

/**
 * Класс управления пользовательскими командами
 */
public class CommandManager {
    /**
     * Словарь используемых команд
     */
    static Map<String, Command> commands = new LinkedHashMap<>();

    /**
     * История последних пользовательских команд
     */
    public static final Map<String, ArrayList<String>> commandsHistory = new HashMap<>();
    /**
     * Количество хранимых последних команд в истории
     */
    static int HISTORY_SIZE = 10;
    /**
     * История названий файлов исполняемых скриптов
     */
    public static final Stack<String> scriptNamesHistory = new Stack<>();

    /**
     * Регистрирует команду
     *
     * @param command добавляемая команда
     */
    public static void registerCommand(Command command) {
        commands.put(command.getCommandName(), command);
    }

    /**
     * Возвращает словарь доступных команд
     *
     * @return словарь доступных команд
     */
    public static Map<String, Command> getCommandsMap() {
        return commands;
    }
//    public static ArrayList<? super Command> getCommandsAsArray(){
//        return (ArrayList<? super Command>) commands.keys();
//    }

    /**
     * Добавляет название исполненной команды в историю
     *
     * @param commandName название команды
     */
    public static void addToHistory(UserData userData, String commandName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (commandsHistory.get(userData.login) == null) {
            commandsHistory.putIfAbsent(userData.login, new ArrayList<>());
            commandsHistory.get(userData.login).add("[" + LocalDateTime.now().format(formatter) + "]" + " - " + commandName);
        } else
            commandsHistory.get(userData.login).add("[" + LocalDateTime.now().format(formatter) + "]" + " - " + commandName);

    }

    /**
     * Возвращает историю последних 10 исполненных команд
     *
     * @return история последних 10 исполненных команд
     */
    public static String getHistory(UserData userData) {
        StringBuilder history = new StringBuilder();
        commandsHistory.get(userData.login)
                .subList((commandsHistory.get(userData.login).size() > 12 ? commandsHistory.get(userData.login).size()-12 : 0),commandsHistory.get(userData.login).size()-1)
                .forEach(c -> history.append(c + ";\n"));
        return (history.toString().isEmpty()) ? "история еще пуста:( " : history.toString();
    }

    /**
     * Исполняет команду
     *
     * @return true, если исполнилась успешно; false, если во время выполнения возникла ошибка
     */
    public static Response isUserExists(UserData userData) {
        try {
            App.logger.info("Проверка, существует ли юзер " + userData.login);
            dbManager.getUserIDByLogin(userData.login);
            System.out.println("Да! Юзер представлен в таблице БД");
            return new Response(UserAuthStatus.OK);
        } catch (SQLException e) {
            App.logger.severe("Юзера " + userData.login + " не существует.");
            App.logger.info("Обновление данных о Users");
            try {
                dbManager.selectUsers();
            } catch (SQLException ex) {
                App.logger.warning("данные о Users не были обновлены.");
            }
            return new Response(UserAuthStatus.NO_SUCH_LOGIN, "данного пользователя не существует( повторите попытку авторизации.");

        }
    }

    public static Response go(Request request) {
        try {
            String CommandName = request.getCommandName();
            String args = request.getCommandStringArg() == null ? "" : request.getCommandStringArg().trim();
            TicketWithMetadata objArg = request.getNewTicketModel();
            if (commands.get(CommandName) == null) throw new NoSuchCommandException();

            Response response;
            if ((commands.get(CommandName).getClass().getSuperclass() != AuthorizationCommand.class)) {
                UserData userData;
                if (objArg != null) {
                    userData = request.getNewTicketModel().userData;
                } else {
                    userData = request.getUserData();
                }
                Response isUserExists = isUserExists(userData);
                if (isUserExists.getResponseStatus() != UserAuthStatus.OK)
                    return new Response(UserAuthStatus.NO_SUCH_LOGIN, "пользователя с логином " + request.getUserData().login + " не существует(");
            }
            if (UserCommand.class.isAssignableFrom(commands.get(CommandName).getClass()) && objArg == null)
                addToHistory(request.getUserData(), CommandName);

            if (commands.get(CommandName).getClass().getSuperclass() == AbstractAddCommand.class
                    && objArg != null) {
                response = ((AbstractAddCommand) commands.get(CommandName)).execute(request.getNewTicketModel());
            } else {
                if (commands.get(CommandName).getClass().getSuperclass() != AuthorizationCommand.class) {
                    response = ((UserCommand) commands.get(CommandName)).execute(request);
                } else {
                    response = ((AuthorizationCommand) commands.get(CommandName)).execute(request.getUserData());
                }
            }

            if (ChangingCollectionCommand.class.isAssignableFrom(commands.get(CommandName).getClass())
                    && response.getResponseStatus() != ResponseStatus.ERROR
                    && response.getResponseStatus() != ResponseStatus.EXIT) {
                ((ChangingCollectionCommand) commands.get(CommandName)).updateCollection();
            }

            return response;
        } catch (NoSuchCommandException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }

    }
}
