package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.common.models.DBModels.TicketWithMetadata;
import org.common.models.Ticket;
import org.server.exceptions.CollectionIdIsTakenException;
import org.server.exceptions.WrongAmountOfArgumentsException;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;
import org.server.utility.managers.CollectionManager;
import org.server.utility.managers.DBInteraction.DBManager;

import java.sql.SQLException;

/**
 * Команда добавления нового элемента коллекции с заданным ключом.
 */
public class InsertCommand extends AbstractAddCommand {
    private Ticket new_ticket;

    public InsertCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("insert {key} {element}", "добавить новый элемент с заданным ключом", collectionManager, dbManager);
        this.new_ticket = new Ticket();
    }

    @Override
    public Response execute(Request request) {
        try {
            String arg = request.getCommandStringArg();
            if (arg.isEmpty()) throw new WrongAmountOfArgumentsException();
            int id = Integer.parseInt(arg.trim());
            if (this.collectionManager.isIdTaken(id)) throw new CollectionIdIsTakenException();
            return new Response(ResponseStatus.OBJECT, ">создание нового экземпляра Ticket:");
        } catch (WrongAmountOfArgumentsException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        } catch (CollectionIdIsTakenException | NumberFormatException e){
            return new Response(ResponseStatus.STOP_SCRIPT, e.getMessage());
        }
    }

    @Override
    public Response execute(TicketWithMetadata newElem) {
        try {
            dbManager.insertIntoTickets(newElem);
            this.collectionManager.addToCollection(newElem);
            return new Response(ResponseStatus.OK, "тоопчик! экземпляр класса Ticket успешно создан и добавлен в коллекцию!");
        }catch (SQLException e) {
            return new Response(ResponseStatus.ERROR, "не удалось добавить экземпляр в бд. айди уже занят(");
        }
      //  return new Response(ResponseStatus.ERROR, "данный айди уже занят(");

    }
}

