package org.client.utility.ModelsAskers;

import org.client.exceptions.NoSuchTicketTypeException;
import org.client.exceptions.WrongFieldTypeException;
import org.client.exceptions.wrongRangeExceptions.WrongPriceRangeException;
import org.client.exceptions.wrongRangeExceptions.WrongXCoordRangeException;
import org.client.exceptions.wrongRangeExceptions.WrongYCoordRangeException;
import org.client.utility.Console;

import org.common.models.Ticket;
import org.common.models.TicketType;
import org.common.models.Coordinates;


import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс запроса и валидации нового экземпляра типа Ticket
 */
public class NewTicketAsker {
    /**
     * Новый экземпляр Ticket
     */
    private final Ticket ticket;
    public Console console;
    
    /**
     * Конструктор
     */
    public NewTicketAsker(Console console) {
        this.ticket = new Ticket();
        this.ticket.set_coordinates(new Coordinates());
        this.console = console;
    }

    /**
     * Валидация имени экземпляра Ticket
     * @param name имя
     * @return true, если имя корректно, иначе false
     */
    public boolean validateName(String name) {
        if (name == null || name.isEmpty()) {
            console.print_error("имя не может быть null (");
            return false;
        }
        this.ticket.set_name(name);
        return true;
    }
    /**
     * Валидация X координаты экземпляра Ticket
     * @param XCoord Х координата
     * @return true, если Х координата корректна, иначе false
     */
    public boolean validateCoordinateX(String XCoord) {
        if (XCoord.isEmpty()) {
            console.print_error("Координата Х не может быть null");
            return false;
        }
        try {
            double i = Double.parseDouble(XCoord.trim());
            if (Coordinates.xMin >= i) throw new WrongXCoordRangeException();

            this.ticket.get_coodinates().set_coordX(i);
            return true;
        } catch (NumberFormatException e) {
            console.print_error("Координата Х должна иметь тип Double)");

        } catch (WrongXCoordRangeException e) {
            console.print_error(e.getMessage());
        }
        return false;
    }
    /**
     * Валидация У координаты экземпляра Ticket
     * @param YCoord Y координата
     * @return true, если Y координата корректна, иначе false
     */
    public boolean validateCoordinateY(String YCoord) {
        if (YCoord.isEmpty()) {
            console.print_error("Координата Y не могут быть null");
            return false;
        }
        try {
            int i = Integer.parseInt(YCoord.trim());
            if (Coordinates.yMax <= i) throw new WrongYCoordRangeException();

            this.ticket.get_coodinates().set_coordY(i);
            return true;
        } catch (NumberFormatException e) {
            console.print_error("Координата Y должна иметь тип Integer)");

        } catch (WrongYCoordRangeException e) {
            console.print_error(e.getMessage());
        }
        return false;
    }

    /**
     * Валидация цены экземпляра Ticket
     * @param price цена
     * @return true, если цена корректна, иначе false
     */
    public boolean validatePrice(String price) {
        try {
            if (price.isEmpty()) {
                this.ticket.set_price(null);
                return true;
            }
            long i = Long.parseLong(price.trim());
            if (i <= 0) throw new WrongPriceRangeException();

            this.ticket.set_price(i);
            return true;
        } catch (NumberFormatException e) {
            console.print_error("Поле price должно иметь тип long...(");
        } catch (WrongPriceRangeException e) {
            console.print_error(e.getMessage());
        }
        return false;
    }
    /**
     * Валидация поля "возвратен ли билет" экземпляра Ticket
     * @param ref поле "возвратен ли билет"
     * @return true, если поле "возвратен ли билет" корректно, иначе false
     */
    public boolean validateRefundable(String ref) {
        try {
            if (!(ref.equalsIgnoreCase("true") || ref.equalsIgnoreCase("false")))
                throw new WrongFieldTypeException();
            this.ticket.set_refundable(Boolean.parseBoolean(ref));
            return true;
        } catch (WrongFieldTypeException e) {
            console.print_error("Поле refundable должно иметь тип boolean");
        }
        return false;

    }
    /**
     * Валидация Типа экземпляра Ticket.
     * @param ttype тип билета
     * @return true, если тип билета корректен, иначе false
     */
    public boolean validateTicketType(String ttype) {
        if (ttype.isEmpty()) {
            console.print_error("Тип билета не может быть null(");
            return false;
        }
        try {
            if (!(TicketType.getNamesArray().contains(ttype.toUpperCase())))
                throw new NoSuchTicketTypeException();

            this.ticket.set_type(TicketType.valueOf(ttype.toUpperCase()));
            return true;

        } catch (NoSuchTicketTypeException e) {
            console.print_error(e.getMessage());
        }
        return false;
    }

    /**
     * Валидация экземпляра Ticket. (Ввод данных из файла .xml)
     * @param collectionManager менеджер коллекции
     * @param ticket экземпляр
     * @return true, если ticket валиден (валидны все поля класса), иначе false
     */

    /**
     * Валидация экземпляра Ticket. (Ввод данных из скрипта)
     * @param id id билета
     * @return валидный экземпляр класса Ticket, иначе null
     */
    public Ticket validateTicketFromScript(int id) {
        try {
        this.ticket.set_id(id);

        String name = console.script_in.nextLine().trim();
        String coordx = console.script_in.nextLine().trim();
        String coordy = console.script_in.nextLine().trim();
        String price = console.script_in.nextLine().trim();
        String ref = console.script_in.nextLine().trim();
        String tickettype = console.script_in.nextLine().trim();
        String person_height = console.script_in.nextLine().trim();
        String person_bd = console.script_in.nextLine().trim();
        PersonAsker personAsker = new PersonAsker(console);
        if (validateName(name) && validateCoordinateX(coordx) && validateCoordinateY(coordy) && validatePrice(price) && validateRefundable(ref)
                && validateTicketType(tickettype) && personAsker.validateHeight(person_height) && personAsker.validateBirthday(person_bd)) {
            this.ticket.set_person(personAsker.getPerson());
            return this.ticket;
        }
        }catch (Exception e){
            return null;
        }
        return null;

    }

    /**
     * Валидация экземпляра Ticket. (Ввод данных из стандартного потока)
     * @param id id нового экземпляра
     * @return новый экземпляр класса Ticket
     */
    public Ticket validateTicketFromInteractiveMode(int id) {

        AtomicBoolean correctName = new AtomicBoolean(false);
        AtomicBoolean correctCoordX = new AtomicBoolean(false);
        AtomicBoolean correctCoordY = new AtomicBoolean(false);
        AtomicBoolean correctPrice = new AtomicBoolean(false);
        AtomicBoolean correctRef = new AtomicBoolean(false);
        AtomicBoolean correctTicketType = new AtomicBoolean(false);

        this.ticket.set_id(id);
        while (!correctName.get()) {
            console.printf("введите имя:" + console.promt2);
            String name = console.in.nextLine().trim();
            correctName.set(this.validateName(name));
        }
        while (!correctCoordX.get()) {
            console.printf("введите координату Х. min знач: " + Coordinates.xMin + console.promt2);
            String x = console.in.nextLine().trim();
            correctCoordX.set(this.validateCoordinateX(x));
        }
        while (!correctCoordY.get()) {
            console.printf("введите координату Y. max знач: " + Coordinates.yMax + console.promt2);
            String y = console.in.nextLine().trim();
            correctCoordY.set(this.validateCoordinateY(y));
        }
        while (!correctPrice.get()) {
            console.printf("введите значение pricе (>0):" + console.promt2);
            String price = console.in.nextLine().trim();
            correctPrice.set(this.validatePrice(price));
        }
        while (!correctRef.get()) {
            console.printf("введите, возвратен ли билет { true/false }: " + console.promt2);
            String ref = console.in.nextLine().trim();
            correctRef.set(this.validateRefundable(ref));
        }
        while (!correctTicketType.get()) {
            console.println("введите тип билета из предложенных:");
            List<String> names = TicketType.getNamesArray();
            for (String name : names) {
                console.println(" - " + name);
            }
            console.printf(console.promt2);
            String tick_type = console.in.nextLine().trim();
            correctTicketType.set(this.validateTicketType(tick_type));
        }

        this.ticket.set_person(new PersonAsker(console).getPerson());
        return ticket;
    }

}
