package fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components;

public class Cpu{
    public int core_units;
    public int units;
    public String family;

    public Cpu(int core_units, int units, String family) {
        this.core_units = core_units;
        this.units = units;
        this.family = family;
    }
}