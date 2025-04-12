package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.common.models.DBModels.TicketWithMetadata;
import org.common.models.Person;
import org.server.exceptions.NoSuchElementException;
import org.server.exceptions.WrongAmountOfArgumentsException;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;
import org.server.utility.managers.CollectionManager;
import org.server.utility.managers.DBInteraction.DBManager;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Команда удаления элементов коллекции, чье поле person эквивалентно заданному
 */
public class RemoveAllByPersonCommand extends ChangingCollectionCommand{
    public RemoveAllByPersonCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("remove_all_by_person {key}", " удалить из коллекции все элементы, значение поля person которого эквивалентно заданному", collectionManager, dbManager);

    }

    @Override
    public Response execute(Request request) {
        try {
            String arg = request.getCommandStringArg();
            if (arg.isEmpty()) throw new WrongAmountOfArgumentsException();
            Integer id = Integer.parseInt(arg.trim());
            if (!this.collectionManager.isIdTaken(id)) throw new NoSuchElementException();

            TreeMap<Integer, TicketWithMetadata> ht = this.collectionManager.getTicketsCollection();
            Person cur_person = this.collectionManager.getById(id).getTicket().get_person();

            StringBuilder resBody = new StringBuilder();
            Set<Map.Entry<Integer, TicketWithMetadata>> entries = ht.entrySet();
            entries.stream().filter(entry -> cur_person.equals(entry.getValue().getTicket().get_person()) && id != entry.getKey())
                    .forEach(entry -> {
                        if (checkIfCanModify(new Request(String.valueOf(entry.getKey()), request.getUserData()))){
                            try {
                                dbManager.deleteById(entry.getKey());
                            } catch (SQLException e) {
                                resBody.append("SQL_ERROR");
                            }
                            this.collectionManager.removeWithId(entry.getKey());
                        resBody.append("элемент с айди="+ entry.getKey() + " был удалён.\n");
                        }
                    });
            if (resBody.toString().contains("SQL_ERROR")) return new Response(ResponseStatus.ERROR, "произошла ошибка при удалении объектов из БД");
            if (resBody.isEmpty()) return new Response(ResponseStatus.OK,"элементов с эквивалентным полем Person не нашлось.. все осталось как есть!");

            return new Response(ResponseStatus.OK, resBody.toString());

        }catch (WrongAmountOfArgumentsException | NoSuchElementException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        } catch (NumberFormatException e) {
            return new Response(ResponseStatus.ERROR, "поле айди должно быть целым числом.(");
        }
    }
}
