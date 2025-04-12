package org.server.models;

import org.common.models.DBModels.TicketWithMetadata;
import org.common.models.DBModels.UserData;
import org.server.App;
import org.server.utility.ModelsValidators.NewTicketValidator;
import org.server.utility.managers.CollectionManager;
import org.server.utility.managers.DBInteraction.Users;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Tickets2 {
    public static ArrayList<TicketWithMetadata> tickets = new ArrayList<>();

    public void addToList(TicketWithMetadata ticket){
        tickets.add(ticket);
    }
    public void clear(){
        tickets.clear();
    }
    public boolean addToCollectionIfOkay(CollectionManager collectionManager) {
    AtomicBoolean are_added = new AtomicBoolean(false);
    StringBuilder added_ids = new StringBuilder();
    for (TicketWithMetadata ticketWithMetadata : tickets) {
        if (new NewTicketValidator().validateTicket(collectionManager, ticketWithMetadata.ticket)) {
            UserData userData = Users.getById(ticketWithMetadata.userData.get_id());
            ticketWithMetadata.setUserData(userData);
            collectionManager.addToCollection(ticketWithMetadata);

            added_ids.append(ticketWithMetadata.getTicket().get_id()+"; ");
            are_added.set(true);
        }
    }
        if (added_ids.isEmpty()) {
            App.logger.warning("коллекция не была заполнена экземплярами из БД");
        } else {
            App.logger.info("в коллекцию были добавлены элементы с id = { " + added_ids + "}");
        }
        return are_added.get();
    }
}
