package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.server.exceptions.WrongAmountOfArgumentsException;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;
import org.server.utility.managers.CommandManager;

/**
 * Команда вывода текущей истории команд.
 */
public class HistoryCommand extends UserCommand{


    public HistoryCommand() {
        super("history", "получить историю о последних <=10 выполненных командах");
    }

    @Override
    public Response execute(Request request) {
        try {
            String arg = request.getCommandStringArg();
            if (!arg.isEmpty()) throw new WrongAmountOfArgumentsException();
            return new Response(ResponseStatus.OK, CommandManager.getHistory(request.getUserData()));
        } catch (WrongAmountOfArgumentsException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage() + " использование: " + this.getCommandName() + " ");
        }
    }
}
