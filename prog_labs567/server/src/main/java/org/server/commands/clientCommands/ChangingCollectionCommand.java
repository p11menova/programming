package org.server.commands.clientCommands;

import org.common.interaction.Request;
import org.server.App;
import org.server.utility.managers.CollectionManager;
import org.server.utility.managers.DBInteraction.DBManager;

import java.sql.SQLException;

public abstract class ChangingCollectionCommand extends UserCommand{
    protected CollectionManager collectionManager;
    protected DBManager dbManager;
    public ChangingCollectionCommand(String name, String description, CollectionManager collectionManager, DBManager dbManager) {
        super(name, description);
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }
    public boolean checkIfCanModify(Request request){
        try{
            int ticket_id = Integer.parseInt(request.getCommandStringArg().trim());
            String login = request.getUserData().login;
            dbManager.getUserIDByLogin(login);
        return (collectionManager.getById(ticket_id).userData.login).equals(login);
        } catch (NumberFormatException | SQLException e){
            return false;
        }
    }
    public boolean updateCollection(){
        App.logger.info("---------------- updating collection ------------------");
        try {
            collectionManager.clear();
            dbManager.selectTickets().addToCollectionIfOkay(collectionManager);
            App.logger.info("---------------- collection successfully refilled ------------------");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
