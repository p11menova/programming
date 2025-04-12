package org.client.utility.ModelsAskers;

import org.common.models.Person;
import org.client.utility.Console;
import org.client.utility.ConsoleMode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс запроса и валидации нового экземпляра класса Person
 */
public class PersonAsker {
    /**
     * Новый экземпляр Person
     */
    private final Person person;
    public Console console;

    /**
     * Конструктор
     */
    public PersonAsker(Console console){
        this.person = new Person();
        this.console = console;
    }

    /**
     * Валидация роста нового экземпляра Person
     * @param height рост
     * @return true, если введенный рост корректен, иначе false
     */
    public boolean validateHeight(String height){
        try {
            double i = Double.parseDouble(height);
            if (i <= 0) {console.print_error("поле height должно быть >0");
                return false;}

            this.person.setHeight(Double.valueOf(height));
            return true;
        }
        catch (NumberFormatException e){
            console.print_error("поле height должно иметь тип double((");
        }
        return false;
    }
    /**
     * Валидация даты рождения нового экземпляра Person
     * @param bh дата рождения
     * @return true, если введенная дата рождения корректна, иначе false
     */
    public boolean validateBirthday(String bh){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
            LocalDate birthday = LocalDate.parse(bh, formatter);
            this.person.setBirthday(birthday.atStartOfDay());
            return true;
        } catch (DateTimeParseException e){
            console.print_error("неверный формат введенных данных (");
        }
        return false;
    }

    /**
     * Запрашивает новый экземпляр класса Person
     */
    public void askPerson(){
        console.println_with_t("создание нового экземпляра класса Person:");
        AtomicBoolean correctHeight = new AtomicBoolean(false);
        while (!correctHeight.get()){
            console.printf_with_t("введите рост (>0)"+console.promt2);
            String height = console.in.nextLine().trim();
            correctHeight.set(validateHeight(height));
        }
        AtomicBoolean correctBirthday = new AtomicBoolean(false);
        while (!correctBirthday.get()){
            console.printf_with_t("введите дату рождения в формате yyyy MM dd"+console.promt2);
            String birthday = console.in.nextLine().trim();
            correctBirthday.set(validateBirthday(birthday));
        }
    }

    /**
     * Возвращает новый экземпляр Person
     * @return новый экземпляр Person
     */
    public Person getPerson(){
        if (console.consoleMode == ConsoleMode.INTERACTIVE) askPerson();
        return this.person;
    }
}
