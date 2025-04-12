package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.common.models.DBModels.TicketWithMetadata;
import org.common.models.Ticket;
import org.server.exceptions.NoSuchElementException;
import org.server.exceptions.WrongAmountOfArgumentsException;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;
import org.server.utility.managers.CollectionManager;
import org.server.utility.managers.DBInteraction.DBManager;

import java.sql.SQLException;
import java.util.*;

/**
 * Команда удаления экземпляров коллекции, превышающих заданный.
 */
public class RemoveIfGreaterCommand extends ChangingCollectionCommand {
    public RemoveIfGreaterCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("remove_greater {element}", " удалить все элементы из коллекции, превышающие заданный", collectionManager, dbManager);
    }

    @Override
    public Response execute(Request request) {
        try {
            String arg = request.getCommandStringArg().trim();
            if (arg.isEmpty()) throw new WrongAmountOfArgumentsException();
            int i = Integer.parseInt(arg);
            if (!this.collectionManager.isIdTaken(i)) throw new NoSuchElementException();
            TreeMap<Integer, TicketWithMetadata> ht = this.collectionManager.getTicketsCollection();
            Ticket cur = ht.get(i).getTicket();
            StringBuilder resBody = new StringBuilder();
            Collection<TicketWithMetadata> values = ht.values();
            values
                    .stream()
                    .filter(v -> cur.compareTo(v.getTicket()) > 0)
                    .forEach(v -> {
                        Request req = new Request("replace", String.valueOf(v.ticket.get_id()));
                        req.setUserData(request.getUserData());
                        if (checkIfCanModify(req)) {
                            try {
                                dbManager.deleteById(v.ticket.get_id());
                                resBody.append("элемент ").append(v.getTicket().get_id()).append(" был удален.");
                            } catch (SQLException e) {
                                resBody.append("SQL_ERROR");
                            }
                        }
                    });

            if (resBody.isEmpty())
                return new Response(ResponseStatus.OK, "все элементы оказались не больше заданного. коллекция осталась без изменений)");
            if (resBody.toString().contains("SQL_ERROR")) return new Response(ResponseStatus.ERROR, "произошла ошибка при удалении из БД. не все экземпляры были удалены.");
            return new Response(ResponseStatus.OK, resBody.toString());
        } catch (WrongAmountOfArgumentsException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        } catch (NumberFormatException e) {
            return new Response(ResponseStatus.ERROR, "айди элемента должен быть целым числом.");
        } catch (NoSuchElementException e) {
            return new Response(ResponseStatus.ERROR, "элемента с указанным айди не существует. использование: " + this.getCommandName());
        }
    }
}
