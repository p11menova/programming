package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.server.exceptions.WrongAmountOfArgumentsException;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;
import org.server.utility.managers.CollectionManager;

/**
 * Команда вывода информации о коллекции.
 */
public class InfoCommand extends UserCommand {
    public InfoCommand() {
        super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
    }

    @Override
    public Response execute(Request request) {
        try {
            String arg = request.getCommandStringArg();
            if (!arg.isEmpty()) throw new WrongAmountOfArgumentsException();
            return new Response(ResponseStatus.OK, CollectionManager.getInfo());
        }
        catch (WrongAmountOfArgumentsException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage() + " использование: " + this.getCommandName());
        }
    }

//    @Override
//    public Response execute(Request request) {
//        return null;
//    }
}
