package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.server.exceptions.CollectionIdIsTakenException;
import org.server.exceptions.WrongAmountOfArgumentsException;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;
import org.server.utility.managers.CollectionManager;
import org.server.utility.managers.DBInteraction.DBManager;

import java.sql.SQLException;

/**
 * Команда удаления элемента коллекции по ключу.
 */
public class RemoveByKeyCommand extends ChangingCollectionCommand{
    public RemoveByKeyCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("remove {key}", "удалить элемент коллекции по ключу", collectionManager, dbManager);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try{
            String arg = request.getCommandStringArg();
            if (arg.isEmpty()) throw new WrongAmountOfArgumentsException();
            int i = Integer.parseInt(arg.trim());
            if (!this.collectionManager.isIdTaken(i)) throw new CollectionIdIsTakenException();
            if (!checkIfCanModify(request)) return new Response(ResponseStatus.ERROR, "у вас нет прав на модификацию данного элемента(");
            try {
                dbManager.deleteById(i);
            } catch (SQLException e) {
                return new Response(ResponseStatus.ERROR, "не удалось удалить элемент из БД(");
            }

            return new Response(ResponseStatus.OK, "элемент номер " + i + " удален из коллекции.");
        } catch (WrongAmountOfArgumentsException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }catch (NumberFormatException e){
            return new Response(ResponseStatus.ERROR, "айди элемента должен быть целым числом(");
        } catch (CollectionIdIsTakenException e) {
            return new Response(ResponseStatus.ERROR, "там и так нечего удалять(");
        }
    }
}
