package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.server.exceptions.WrongFileRightException;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Команда исполнения скрипта
 */
public class ExecuteScriptCommand extends UserCommand {
    public ExecuteScriptCommand() {
        super("execute_script file_name", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");

    }

    @Override
    public Response execute(Request request) {
        try {
            String arg = request.getCommandStringArg();
            File file = new File(arg.trim());
            if (!file.exists()) throw new FileNotFoundException();
            if (!(file.canRead())){
                throw new WrongFileRightException();
            }
            return new Response(ResponseStatus.SCRIPT, arg.trim());
//            Console.readScriptInput(in, file.getName());
//            CommandManager.scriptNamesHistory.pop();
        } catch (FileNotFoundException e) {
            return new Response(ResponseStatus.ERROR, "файл не найден..");
        } catch (WrongFileRightException e) {
            return new Response(ResponseStatus.ERROR, "отказано в правах доступа( чтение из файла невозможно.");

        }
    }
}
