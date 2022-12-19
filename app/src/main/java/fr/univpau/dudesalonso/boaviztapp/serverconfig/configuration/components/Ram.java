package fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components;

import java.io.Serializable;

public class Ram implements Serializable {
    public int units;
    public int capacity;
    public String manufacturer;

    public Ram(int units, int capacity, String manufacturer) {
        this.units = units;
        this.capacity = capacity;
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Ram{" +
                "units=" + units +
                ", capacity=" + capacity +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}