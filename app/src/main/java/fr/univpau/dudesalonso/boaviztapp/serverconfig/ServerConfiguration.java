package fr.univpau.dudesalonso.boaviztapp.serverconfig;

import com.google.gson.Gson;

import java.io.Serializable;

import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.Configuration;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.model.Model;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.usage.Usage;

public class ServerConfiguration implements Serializable {
    public Model model;
    public Configuration configuration;
    public Usage usage;

    public ServerConfiguration(Model model, Configuration configuration, Usage usage) {
        this.model = model;
        this.configuration = configuration;
        this.usage = usage;
    }

    public String getAsJson()  {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "ServerConfiguration{" +
                "model=" + model +
                ", configuration=" + configuration +
                ", usage=" + usage +
                '}';
    }
}

