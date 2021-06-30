package org.jcode.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Global {
    private String name;
    private long currentConfirmed;
    private long currentDeaths;
    private long currentRecovered;
    private Map<String, Map<String, Long>> time_series_data;

    public Global(){
        this.name = "global";
        this.time_series_data = new HashMap<>();
        this.time_series_data.put("confirmed", new HashMap<>());
        this.time_series_data.put("deaths", new HashMap<>());
        this.time_series_data.put("recovered", new HashMap<>());
    }
}
