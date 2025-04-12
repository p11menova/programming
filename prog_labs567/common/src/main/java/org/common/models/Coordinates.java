package org.common.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс Координат (x, y)
 */
public class Coordinates implements Serializable {
    public static int xMin = -957;
    public static int yMax = 979;
    @JsonProperty("x")
    private Double x; //Поле не может быть null
    @JsonProperty("y")
    private int y; //Максимальное значение поля: 979
    @JsonIgnore
    public Double get_CoordX(){
        return this.x;
    }
    @JsonIgnore
    public int get_CoordY(){
        return this.y;
    }
    public void set_coordX(Double x){
        this.x = x;
    }
    public void set_coordY(int y){
        this.y = y;
    }
    public void set_coords(String coords){
        StringBuilder sb = new StringBuilder(coords);
        sb = sb.deleteCharAt(coords.length()-1);
        sb = sb.deleteCharAt(0);
        String[] c = sb.toString().split(",");

        set_coordX(Double.parseDouble(c[0].trim()));
        set_coordY(Integer.parseInt(c[1].trim()));



    }
    @Override
    public String toString() {
        return "(" +
                x + ", "+ y+
                ')';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) return false;
        return Objects.equals(this.x, ((Coordinates) obj).get_CoordX()) &&
                Objects.equals(this.y, ((Coordinates) obj).get_CoordY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
