package fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components;

public class Disk{
    public int units;
    public String type;
    public int capacity;
    public String manufacturer;

    public Disk(int units, String type, int capacity, String manufacturer) {
        this.units = units;
        this.type = type;
        this.capacity = capacity;
        this.manufacturer = manufacturer;
    }
}