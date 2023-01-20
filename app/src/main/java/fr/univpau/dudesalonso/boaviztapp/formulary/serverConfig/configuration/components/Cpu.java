package fr.univpau.dudesalonso.boaviztapp.formulary.serverConfig.configuration.components;

import java.io.Serializable;

public class Cpu implements Serializable {
    public int core_units;
    public int units;
    public String family;
    public int tdp;

    public Cpu( int units, int tdp, int core_units, String family) {
        this.core_units = core_units;
        this.units = units;
        this.family = family;
        this.tdp = tdp;
    }

    @Override
    public String toString() {
        return "Cpu{" +
                "core_units=" + core_units +
                ", units=" + units +
                ", family='" + family + '\'' +
                ", tdp=" + tdp +
                '}';
    }
}