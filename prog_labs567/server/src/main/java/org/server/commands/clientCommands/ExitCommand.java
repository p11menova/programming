package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.server.exceptions.WrongAmountOfArgumentsException;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;

/**
 * Команда завершения работы программы (без сохранения в файл).
 */
public class ExitCommand extends UserCommand{
    public ExitCommand() {
        super("exit", "завершить программу (без сохранения в файл)");
    }

    @Override
    public Response execute(Request request) {
        try {
            String arg = request.getCommandStringArg();
            if (!arg.isEmpty()) throw new WrongAmountOfArgumentsException();
            return new Response(ResponseStatus.EXIT, "ЗАВЕРШЕНИЕ РАБОТЫ КЛИЕНТА)");
        } catch (WrongAmountOfArgumentsException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage() + " использование: " + this.getCommandName() + " ");
        }
    }
}
