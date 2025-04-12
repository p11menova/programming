package org.common.models.DBModels;

import org.common.models.Ticket;

import java.io.Serializable;

public class TicketWithMetadata implements Serializable {
    public Ticket ticket;
    public UserData userData;
    public TicketWithMetadata(Ticket ticket){
        this.ticket = ticket;
    }
    public void setUserData(UserData userData){
        this.userData = userData;
    }
    public Ticket getTicket(){
        return this.ticket;
    }
    public void setUserId(int id){
        userData.id = id;
    }
    @Override
    public String toString() {
        return String.format("%12s | ", userData.login) + getTicket().toString()+"\n";
    }
}
