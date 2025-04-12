package org.common.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.common.utility.ZonedDateTimeDeserializer;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Класс Ticket
 */
public class Ticket implements Serializable, Comparable<Ticket>{
    @JsonProperty("id")
    private Integer id;
    //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @JsonProperty("name")
    private String name; //Поле не может быть null, Строка не может быть пустой
    @JsonProperty("coordinates")
    private Coordinates coordinates; //Поле не может быть null

    @JsonProperty("creationDate")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm")
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    // Doesn't exist, So I created a custom ZonedDateDeserializer utility class.
    private ZonedDateTime creationDate; //Пол не может быть null, Значение этого поля должно генерироваться автоматически
    @JsonProperty("price")
    private Long price; //Поле может быть null, Значение поля должно быть больше 0
    @JsonProperty("refundable")
    private Boolean refundable; //Поле не может быть null
    @JsonProperty("tickettype")
    private TicketType type; //Поле не может быть null
    @JsonProperty("person")
    private Person person; //Поле не может быть null
    public void set_id(Integer id){
        this.id = id;
    }
    @JsonIgnore
    public Integer get_id(){
        return this.id;
    }
    public void set_name(String name){
        this.name = name;
    }
    @JsonIgnore
    public String get_name(){
        return this.name;
    }
    public void set_coordinates(Coordinates coor){
        this.coordinates = coor;
    }
    @JsonIgnore
    public Coordinates get_coodinates(){
        return this.coordinates;
    }
    public void set_creationData(ZonedDateTime creationDate){
        this.creationDate = creationDate;
    }
    @JsonIgnore
    public ZonedDateTime get_creationDate(){
        return this.creationDate;
    }
    public void set_price(Long price){
        this.price = price;
    }
    @JsonIgnore
    public Long get_price(){
        return this.price;
    }
    public void set_refundable(Boolean refundable){
        this.refundable = refundable;
    }
    @JsonIgnore
    public Boolean get_refundable(){
        return this.refundable;
    }
    public void set_type(TicketType ticketype){
        this.type = ticketype;
    }
    @JsonIgnore
    public TicketType get_ticketType(){
        return this.type;
    }
    public void set_person(Person p){
        this.person = p;
    }
    @JsonIgnore
    public Person get_person(){
        return this.person;
    }
    public Ticket(){
        this.creationDate = ZonedDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("%6d | %10s | %14s | %20s | %6d | %14s | %8s | ",
                id,
                name,
                coordinates.toString(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm").format(creationDate),
                price,
                refundable,
                type) + person.toString();
    }

    @Override
    public int compareTo(Ticket o) {
        int res = Integer.compare(this.get_ticketType().ordinal(), o.get_ticketType().ordinal());
        if (res == 0){
            return Integer.compare(this.get_id(), o.get_id());
        }
        else return res;
    }
}

