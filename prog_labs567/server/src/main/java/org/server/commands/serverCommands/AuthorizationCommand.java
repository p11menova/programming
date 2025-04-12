package org.server.commands.serverCommands;

import org.common.interaction.Response;
import org.common.models.DBModels.UserData;
import org.server.commands.Command;
import org.server.utility.managers.DBInteraction.DBManager;

public abstract class AuthorizationCommand implements Command {
    protected String commandName;
    protected DBManager dbManager;
    public AuthorizationCommand(String commandName, DBManager dbManager) {
        this.commandName = commandName;
        this.dbManager = dbManager;

    }
    public String getCommandName(){
        return this.commandName;
    }
    public abstract Response execute(UserData userData);
}
