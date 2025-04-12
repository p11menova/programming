package org.server.utility.ModelsValidators;

import org.common.models.Coordinates;
import org.common.models.Ticket;
import org.server.exceptions.wrongRangeExceptions.WrongPriceRangeException;
import org.server.exceptions.wrongRangeExceptions.WrongXCoordRangeException;
import org.server.exceptions.wrongRangeExceptions.WrongYCoordRangeException;
import org.server.utility.managers.CollectionManager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс запроса и валидации нового экземпляра типа Ticket
 */
public class NewTicketValidator {
    /**
     * Валидация имени экземпляра Ticket
     *
     * @param name имя
     * @return true, если имя корректно, иначе false
     */
    public boolean validateName(String name) {
        if (name == null || name.isEmpty())
            return false;
        return true;
    }

    /**
     * Валидация X координаты экземпляра Ticket
     *
     * @param XCoord Х координата
     * @return true, если Х координата корректна, иначе false
     */
    public boolean validateCoordinateX(String XCoord) {
        if (XCoord.isEmpty()) {
            return false;
        }
        ;
        try {
            double i = Double.parseDouble(XCoord.trim());
            if (Coordinates.xMin >= i) throw new WrongXCoordRangeException();

  //          this.ticket.get_coodinates().set_coordX(i);
            return true;
        } catch (NumberFormatException | WrongXCoordRangeException e) {
            return false;

        }

    }

    /**
     * Валидация У координаты экземпляра Ticket
     *
     * @param YCoord Y координата
     * @return true, если Y координата корректна, иначе false
     */
    public boolean validateCoordinateY(String YCoord) {
        if (YCoord.isEmpty()) {
            return false;
        }
        ;
        try {
            int i = Integer.parseInt(YCoord.trim());
            if (Coordinates.yMax <= i) throw new WrongYCoordRangeException();

    //        this.ticket.get_coodinates().set_coordY(i);
            return true;
        } catch (NumberFormatException | WrongYCoordRangeException e) {
            return false;
        }
    }

    /**
     * Валидация цены экземпляра Ticket
     *
     * @param price цена
     * @return true, если цена корректна, иначе false
     */
    public boolean validatePrice(String price) {
        try {
            if (price.isEmpty()) {
         //       this.ticket.set_price(null);
                return true;
            }
            long i = Long.parseLong(price.trim());
            if (i <= 0) throw new WrongPriceRangeException();

//            this.ticket.set_price(i);
            return true;
        } catch (NumberFormatException | WrongPriceRangeException e) {
            return false;}
    }


    /**
     * Валидация экземпляра Ticket. (Ввод данных из файла .xml)
     *
     * @param collectionManager менеджер коллекции
     * @param ticket            экземпляр
     * @return true, если ticket валиден (валидны все поля класса), иначе false
     */
    public boolean validateTicket(CollectionManager collectionManager, Ticket ticket) {
        AtomicBoolean is_okay = new AtomicBoolean(false);
        if (collectionManager.isIdTaken(ticket.get_id())) {
            return false;
        }
        NewPersonValidator personValidator = new NewPersonValidator();
        is_okay.set(
                validateName(ticket.get_name()) &&
                        validateCoordinateX(ticket.get_coodinates().get_CoordX().toString()) &&
                        validateCoordinateY(Integer.toString(ticket.get_coodinates().get_CoordY())) &&
                        validatePrice((ticket.get_price() == null) ? "" : ticket.get_price().toString()) &&
                        personValidator.validateHeight(ticket.get_person().getHeight().toString()) &&
                        personValidator.validateBirthday(ticket.get_person().getBirthday().toString()));
        return is_okay.get();
    }

    /**
     * Валидация экземпляра Ticket. (Ввод данных из скрипта)
     * @param id id билета
     * @return валидный экземпляр класса Ticket, иначе null
     */
//    public Ticket validateTicketFromScript(int id) {
//        this.ticket.set_id(id);
//
//        String name = Console.script_in.nextLine().trim();
//        String coordx = Console.script_in.nextLine().trim();
//        String coordy = Console.script_in.nextLine().trim();
//        String price = Console.script_in.nextLine().trim();
//        String ref = Console.script_in.nextLine().trim();
//        String tickettype = Console.script_in.nextLine().trim();
//        String person_height = Console.script_in.nextLine().trim();
//        String person_bd = Console.script_in.nextLine().trim();
//        NewPersonValidator personAsker = new NewPersonValidator();
//        if (validateName(name) && validateCoordinateX(coordx) && validateCoordinateY(coordy) && validatePrice(price) && validateRefundable(ref)
//                && validateTicketType(tickettype) && personAsker.validateHeight(person_height) && personAsker.validateBirthday(person_bd)) {
//            this.ticket.set_person(personAsker.getPerson());
//            return this.ticket;
//        }
//        return null;
//
//    }

    /**
     * Валидация экземпляра Ticket. (Ввод данных из стандартного потока)
     * @param id id нового экземпляра
     * @return новый экземпляр класса Ticket
     */
//    public Ticket validateTicketFromInteractiveMode(int id) {
//
//        AtomicBoolean correctName = new AtomicBoolean(false);
//        AtomicBoolean correctCoordX = new AtomicBoolean(false);
//        AtomicBoolean correctCoordY = new AtomicBoolean(false);
//        AtomicBoolean correctPrice = new AtomicBoolean(false);
//        AtomicBoolean correctRef = new AtomicBoolean(false);
//        AtomicBoolean correctTicketType = new AtomicBoolean(false);
//
//        this.ticket.set_id(id);
//        while (!correctName.get()) {
//            Console.printf("введите имя:" + Console.promt2);
//            String name = Console.in.nextLine().trim();
//            correctName.set(this.validateName(name));
//        }
//        while (!correctCoordX.get()) {
//            Console.printf("введите координату Х. min знач: " + Coordinates.xMin + Console.promt2);
//            String x = Console.in.nextLine().trim();
//            correctCoordX.set(this.validateCoordinateX(x));
//        }
//        while (!correctCoordY.get()) {
//            Console.printf("введите координату Y. max знач: " + Coordinates.yMax + Console.promt2);
//            String y = Console.in.nextLine().trim();
//            correctCoordY.set(this.validateCoordinateY(y));
//        }
//        while (!correctPrice.get()) {
//            Console.printf("введите значение pricе (>0):" + Console.promt2);
//            String price = Console.in.nextLine().trim();
//            correctPrice.set(this.validatePrice(price));
//        }
//        while (!correctRef.get()) {
//            Console.printf("введите, возвратен ли билет { true/false }: " + Console.promt2);
//            String ref = Console.in.nextLine().trim();
//            correctRef.set(this.validateRefundable(ref));
//        }
//        while (!correctTicketType.get()) {
//            Console.println("введите тип билета из предложенных:");
//            List<String> names = TicketType.getNamesArray();
//            for (String name : names) {
//                Console.println(" - " + name);
//            }
//            Console.printf(Console.promt2);
//            String tick_type = Console.in.nextLine().trim();
//            correctTicketType.set(this.validateTicketType(tick_type));
//        }
//
//        this.ticket.set_person(new PersonAsker(console).getPerson());
//        return ticket;
//    }

}
