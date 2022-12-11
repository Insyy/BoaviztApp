package fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration;

import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components.Cpu;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components.Disk;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components.PowerSupply;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components.Ram;

import java.util.ArrayList;

public class Configuration{
    public Cpu cpu;
    public ArrayList<Ram> ram;
    public ArrayList<Disk> disk;
    public PowerSupply power_supply;
}