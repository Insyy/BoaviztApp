package fr.univpau.dudesalonso.boaviztapp.formulary.serverConfig.configuration;

import fr.univpau.dudesalonso.boaviztapp.formulary.serverConfig.configuration.components.Cpu;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverConfig.configuration.components.Disk;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverConfig.configuration.components.Ram;

import java.io.Serializable;
import java.util.ArrayList;

public class Configuration implements Serializable {
    public Cpu cpu;
    public ArrayList<Ram> ram;
    public ArrayList<Disk> disk;

    public Configuration(Cpu cpu, ArrayList<Ram> ram, ArrayList<Disk> disk) {
        this.cpu = cpu;
        this.ram = ram;
        this.disk = disk;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "cpu=" + cpu +
                ", ram=" + ram +
                ", disk=" + disk +
                '}';
    }
}