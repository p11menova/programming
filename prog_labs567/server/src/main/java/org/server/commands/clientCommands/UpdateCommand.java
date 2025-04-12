package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.common.models.DBModels.TicketWithMetadata;
import org.server.exceptions.NoSuchElementException;
import org.server.exceptions.WrongAmountOfArgumentsException;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;
import org.server.utility.managers.CollectionManager;
import org.server.utility.managers.DBInteraction.DBManager;

import java.sql.SQLException;


/**
 * Команда обновления элемента коллекции по заданному ключу.
 */
public class UpdateCommand extends AbstractAddCommand {
    private int idToBeUpdated;
    public UpdateCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("update {key} {element}", " обновить значение элемента коллекции, айди которого равен заданному", collectionManager, dbManager);
    }
    @Override
    public Response execute(Request request) {
        try {
            String arg = request.getCommandStringArg();
            System.out.println(request.getUserData());
            if (arg.isEmpty()) throw new WrongAmountOfArgumentsException();
            int id = Integer.parseInt(arg.trim());
            if (!this.collectionManager.isIdTaken(id)) throw new NoSuchElementException();
            if (!checkIfCanModify(request)) return new Response(ResponseStatus.ERROR, "у вас нет прав доступа на модификацию данного элемента!(");
            this.idToBeUpdated = id;
            return new Response(ResponseStatus.OBJECT, ">обновление экземпляра Ticket:");
        } catch (WrongAmountOfArgumentsException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        } catch (NoSuchElementException e){
            return new Response(ResponseStatus.STOP_SCRIPT, e.getMessage());
        }
    }
    @Override
    public Response execute(TicketWithMetadata newElem) {
        try {
            dbManager.updateTicket(newElem);
            collectionManager.updateElem(newElem);
        } catch (SQLException e) {
            return new Response(ResponseStatus.ERROR, "не удалось обновить данный элемент(");
        }
        return new Response(ResponseStatus.OK, "тоопчик! элемент с айди=" + this.idToBeUpdated + " успешно обновлен!");

    }
}
