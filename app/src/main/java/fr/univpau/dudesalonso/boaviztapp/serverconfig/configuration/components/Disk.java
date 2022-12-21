package fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components;

import java.io.Serializable;

public class Disk implements Serializable {
    public int units;
    public String type;
    public int capacity;
    public String manufacturer = null;

    public Disk(int units, String type, int capacity, String manufacturer) {
        this.units = units;
        this.type = type;
        this.capacity = capacity;
        this.manufacturer = manufacturer;
    }

    public Disk(int units, String type, int capacity) {
        this.units = units;
        this.type = type;
        this.capacity = capacity;

        if (type.equals("hdd"))
            this.capacity *= 1000;
    }

    @Override
    public String toString() {
        return "Disk{" +
                "units=" + units +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}