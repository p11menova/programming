package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.common.models.Coordinates;
import org.common.models.DBModels.TicketWithMetadata;
import org.common.models.Ticket;
import org.server.exceptions.WrongAmountOfArgumentsException;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;
import org.server.utility.managers.CollectionManager;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Команда группировки элементов коллекции по значению поля Coordinates
 */
public class GroupCountingByCoordinatesCommand extends UserCommand{
    public CollectionManager collectionManager;
    public GroupCountingByCoordinatesCommand(CollectionManager collectionManager) {
        super("group_counting_by_coordinates", "сгруппировать элементы коллекции по значению поля coordinates, вывести количество элементов в каждой группе");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            String arg = request.getCommandStringArg();
            if (!arg.isEmpty()) throw new WrongAmountOfArgumentsException();
            TreeMap<Integer, TicketWithMetadata> ht1 = this.collectionManager.getTicketsCollection();
            TreeMap<Integer, Ticket> ht = new TreeMap<>();

            while (ht1.descendingKeySet().iterator().hasNext()){
                Integer key = ht.descendingKeySet().iterator().next();
                ht.put(key, ht1.get(key).ticket);
            }
            if (ht.isEmpty()) {
                return new Response(ResponseStatus.OK, "коллекция еще пуста.");
            }
            StringBuilder resBody = new StringBuilder();
            Map<Coordinates, List<Ticket>> hm = ht.values().stream().collect(Collectors.groupingBy(Ticket::get_coodinates));
            hm.forEach((key,value) -> resBody.append(key.toString() + ": количество элементов с такими коорд.:" + value.size()+"\n"));
            return new Response(ResponseStatus.OK, resBody.toString().trim());
        } catch (WrongAmountOfArgumentsException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }
    }
}
