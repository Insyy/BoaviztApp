package fr.univpau.dudesalonso.boaviztapp.serverconfig;

import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.Configuration;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.model.Model;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.usage.Usage;

/*import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;*/

public class ServerConfiguration{
    public Model model;
    public Configuration configuration;
    public Usage usage;

    public ServerConfiguration(Model model, Configuration configuration, Usage usage) {
        this.model = model;
        this.configuration = configuration;
        this.usage = usage;
    }

    /*public String getAsJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }*/
}

