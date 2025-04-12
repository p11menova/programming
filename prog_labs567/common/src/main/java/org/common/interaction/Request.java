package org.common.interaction;

import org.common.models.DBModels.TicketWithMetadata;
import org.common.models.DBModels.UserData;


import java.io.Serializable;

public class Request implements Serializable {
    private String CommandName;
    private String CommandStringArg;
    private UserData userData;

    private TicketWithMetadata NewTicketModel; // при <? extends AbstractAddCommand>
    public Request(String cn,  TicketWithMetadata newTick){
        this.CommandName = cn;
        this.NewTicketModel = newTick;
    }
    public Request(String cn, String ca){
        this.CommandName = cn;
        this.CommandStringArg = ca;
        this.NewTicketModel = null;
    }
    public Request(String cn, UserData userData){
        this.CommandName = cn;
        this.userData = userData;
    }
    public Request(){
        new Request("", "");
    }

    public String getCommandName() {
        return CommandName;
    }
    public String getCommandStringArg(){
        return CommandStringArg;
    }
    public TicketWithMetadata getNewTicketModel() {
        return NewTicketModel;
    }
    public UserData getUserData(){
        return userData;
    }
    public void setUserData(UserData userData){
        this.userData = userData;
    }

}