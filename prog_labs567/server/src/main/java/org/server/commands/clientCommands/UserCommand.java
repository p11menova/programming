package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.common.interaction.Response;
import org.server.commands.Command;

/**
 * Абстрактный класс команды. Служит родителем для всех пользовательских команд.
 */
public abstract class UserCommand implements Command {
    protected String CommandName;
    protected String CommandDescription;
    protected Response response;

    public UserCommand(String name, String description){
        this.CommandName = name.split(" ",2)[0];
        this.CommandDescription = description;
    }

    /**
     * Исполняет команду
     * @return true, если команда выполнилась успешно, иначе false
     */

    //public abstract boolean go(String arg);
    public abstract Response execute(Request request);
    public String getCommandName(){
        return this.CommandName;
    }
    protected String getCommandDescription(){
        return this.CommandDescription;
    }
    @Override
    public String toString() {
        return  CommandName + " : " + CommandDescription;
    }
}

