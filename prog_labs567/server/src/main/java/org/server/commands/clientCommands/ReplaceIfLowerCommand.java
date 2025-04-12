package org.server.commands.clientCommands;


import org.common.interaction.Request;
import org.common.models.DBModels.TicketWithMetadata;
import org.server.exceptions.NoSuchElementException;
import org.server.exceptions.WrongAmountOfArgumentsException;
import org.common.interaction.Response;
import org.common.interaction.ResponseStatus;
import org.server.utility.managers.CollectionManager;
import org.server.utility.managers.DBInteraction.DBManager;

/**
 * Команда замены экземпляра коллекции, на новый, если он меньше старого.
 */
public class ReplaceIfLowerCommand extends AbstractAddCommand {
    private int idToBeReplaced;
    public ReplaceIfLowerCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("replace_if_lower {key} {element}",
                "заменить значение по ключу, если новое значение меньше старого",
                collectionManager, dbManager);
    }

    @Override
    public Response execute(TicketWithMetadata newElem) {
        if (this.collectionManager.getById(this.idToBeReplaced).getTicket().compareTo(newElem.getTicket()) > 0) {
            this.collectionManager.addToCollection(newElem);
            return new Response(ResponseStatus.OK, "введенный элемент меньше предыдущего. произошла замена!");
        }
        return new Response(ResponseStatus.OK, "введенный элемент не меньше предыдущего.) все осталось как было.");
    }

    @Override
    public Response execute(Request request) {
        try {
            String arg = request.getCommandStringArg().trim();
            System.out.println(request.getCommandStringArg()+" " + request.getCommandStringArg().getClass());
            if (arg.isEmpty()) throw new WrongAmountOfArgumentsException();
            System.out.println("бля я тут");
            int id = Integer.parseInt(arg);
            System.out.println("бля я тут");

            if (!this.collectionManager.isIdTaken(id)) throw new NoSuchElementException();
            if (!checkIfCanModify(request)) return new Response(ResponseStatus.ERROR, "у вас нет прав доступа на модификацию данного элемента!(");
            this.idToBeReplaced = id;
            return new Response(ResponseStatus.OBJECT, ">создание нового экземпляра Ticket, на который хотите заменить:");
        } catch (WrongAmountOfArgumentsException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage());
        } catch (NumberFormatException  e) {
            return new Response(ResponseStatus.STOP_SCRIPT, "ключ элемента должен быть целым числом(");
        } catch (NoSuchElementException e ){
            return new Response(ResponseStatus.STOP_SCRIPT, "элемента с таким айди еще нет(");
        }
    }
}

