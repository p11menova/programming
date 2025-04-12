package org.server.commands.clientCommands;

import org.common.interaction.Response;
import org.common.models.DBModels.TicketWithMetadata;
import org.server.utility.managers.CollectionManager;
import org.server.utility.ModelsValidators.NewTicketValidator;
import org.server.utility.managers.DBInteraction.DBManager;

/**
 * Абстрактный класс команды добавления экземпляра.
 * Служит родителем, если в команде требуется запрос и/или валидация нового экземпляра класса Ticket.
 */
public abstract class AbstractAddCommand extends ChangingCollectionCommand {
    NewTicketValidator ticketValidator;
    public AbstractAddCommand(String CommandName, String CommandDescription, CollectionManager collectionManager, DBManager dbManager) {
        super(CommandName, CommandDescription, collectionManager, dbManager);
        this.ticketValidator = new NewTicketValidator();
    }
    public abstract Response execute(TicketWithMetadata newTicket);
//    public boolean validate(Ticket newElem){
//        return this.ticketValidator.validateTicket(collectionManager, newElem);
//    }

}
