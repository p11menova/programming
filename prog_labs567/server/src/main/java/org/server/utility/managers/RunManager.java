package org.server.utility.managers;

import org.server.App;
import org.server.commands.Command;
import org.server.commands.clientCommands.*;
import org.server.commands.serverCommands.LoginUserCommand;
import org.server.commands.serverCommands.RegisterUserCommand;
import org.server.utility.managers.DBInteraction.DBManager;

import java.sql.SQLException;

/**
 * Менеджер запуска приложения
 */
public class RunManager {
    /**
     * Запускает работу приложения: проверка установки переменной окружения, регистрация команд, запуск чтения пользовательского ввода
     */
    private CollectionManager collectionManager = new CollectionManager();
    public static  DBManager dbManager;
    private final String host;
    private final String login;
    private final String password;
    public RunManager(String host, String login, String password){
        this.host = host;
        this.password = password;
        this.login = login;
    }
    public void prepare() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            dbManager = new DBManager(host, login, password);
            dbManager.connectToDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    //    fileManager = new FileManager(path);
        try {
            dbManager.selectTickets().addToCollectionIfOkay(collectionManager);
        } catch (SQLException e) {
            App.logger.warning("ошибка при прочтении коллекции из бд");
        }
        //-- user commands --
        CommandManager.registerCommand(new HelpCommand());
        CommandManager.registerCommand(new InfoCommand());
        CommandManager.registerCommand(new ShowCommand(collectionManager));
        CommandManager.registerCommand(new InsertCommand(collectionManager, dbManager));
        CommandManager.registerCommand(new UpdateCommand(collectionManager, dbManager));
        CommandManager.registerCommand(new RemoveByKeyCommand(collectionManager, dbManager));
        CommandManager.registerCommand(new ClearCommand(collectionManager, dbManager));
        // CommandManager.registerCommand(new SaveCommand(collectionManager, fileManager));
        CommandManager.registerCommand(new ExecuteScriptCommand());
        CommandManager.registerCommand(new HistoryCommand());
        CommandManager.registerCommand(new ExitCommand());
        CommandManager.registerCommand(new RemoveIfGreaterCommand(collectionManager, dbManager));
        //CommandManager.registerCommand(new ReplaceIfGreaterCommand(collectionManager,dbManager));
        //CommandManager.registerCommand(new ReplaceIfLowerCommand(collectionManager,dbManager));
        CommandManager.registerCommand(new RemoveAllByPersonCommand(collectionManager, dbManager));
        //CommandManager.registerCommand(new GroupCountingByCoordinatesCommand(collectionManager));

        //-- auth commands--
        CommandManager.registerCommand(new RegisterUserCommand(dbManager));
        CommandManager.registerCommand(new LoginUserCommand(dbManager));

    }
}