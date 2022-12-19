package fr.univpau.dudesalonso.boaviztapp.serverconfig.model;

import java.io.Serializable;

public class Model implements Serializable {
    public String type;

    public Model(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Model{" +
                "type='" + type + '\'' +
                '}';
    }
}
