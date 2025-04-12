//package org.server.models;
//
//import com.fasterxml.jackson.dataformat.xml.annotation.*;
//
//import org.example.models.Ticket;
//import org.server.utility.managers.CollectionManager;
//import org.server.utility.ModelsValidators.NewTicketValidator;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicBoolean;
//
///**
// * Класс списка билетов (заполняется при чтении файла .xml)
// */
//@JacksonXmlRootElement(localName = "tickets")
//public class Tickets {
//    @JacksonXmlProperty(localName = "ticket")
//    @JacksonXmlElementWrapper(useWrapping = false)
//
//    private List<Ticket> tickets = new ArrayList<>();
//
//    public void addToList(Ticket ticket) {
//        this.tickets.add(ticket);
//    }
//
//    /**
//     * Добавляет валидные элементы списка Tickets в коллекцию
//     *
//     * @param collectionManager менеджер коллекции
//     * @return true, если был добавлен хоть один билет, false, если все не валидны
//     */
//    public boolean addToCollectionIfOkay(CollectionManager collectionManager) {
//        AtomicBoolean are_added = new AtomicBoolean(false);
//        for (Ticket ticket : tickets) {
//            if (new NewTicketValidator().validateTicket(collectionManager, ticket)) {
//                collectionManager.addToCollection(ticket);
//                are_added.set(true);
//            }
//        }
//        return are_added.get();
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder result = new StringBuilder();
//        for (Ticket ticket : tickets) {
//            result.append("\n").append(ticket.toString());
//        }
//        return result.toString();
//    }
//}
