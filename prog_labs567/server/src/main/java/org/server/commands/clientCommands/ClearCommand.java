package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.server.exceptions.WrongAmountOfArgumentsException;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;
import org.server.utility.managers.CollectionManager;
import org.server.utility.managers.DBInteraction.DBManager;

import java.sql.SQLException;


/**
 * Команда очистки коллекции.
 */
public class ClearCommand extends ChangingCollectionCommand {
    public ClearCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("clear", "очистить коллекцию (будут удалены объекты, созданные данным пользователем)", collectionManager, dbManager);

    }

    @Override
    public Response execute(Request request) {
        try {
            String arg = request.getCommandStringArg();
            if (!arg.isEmpty()) throw new WrongAmountOfArgumentsException();
            try {
                System.out.println(request.getUserData().login + " " + request.getUserData().id);
                String res = dbManager.clearByUser(request.getUserData().id);

                return new Response(ResponseStatus.OK, res.trim().isEmpty() ?
                        "вы еще не создавали элементы) " :
                        "элементы коллекции, добавленные этим юзером были удалены их айди:\n"+res);
            } catch (SQLException e) {
                return new Response(ResponseStatus.ERROR, "коллекция не очищена");
            }

        } catch (WrongAmountOfArgumentsException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage() + " использование: " + this.getCommandName() + " ");
        }
    }
}
