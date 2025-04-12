package org.server.commands.serverCommands;

import org.server.commands.Command;

public abstract class ServerCommand implements Command {
    protected String CommandName;

    public ServerCommand(String commandName) {
        this.CommandName = commandName;
    }
    public String getCommandName(){return this.CommandName;}
    public abstract boolean go();
}
