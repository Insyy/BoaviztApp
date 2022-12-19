package fr.univpau.dudesalonso.boaviztapp.serverconfig.usage;

import java.io.Serializable;

public class Usage implements Serializable {
    public int days_use_time;
    public int hours_use_time;
    public int years_use_time;
    public String usage_location;

    public int hours_electrical_consumption;
    public int time_workload;

    public Usage(int years_use_time, String usage_location, int value,  String methodMode) {
        this.days_use_time = 0;
        this.hours_use_time = 0;
        this.years_use_time = years_use_time;
        this.usage_location = usage_location;
        if (methodMode.equals("Electricity"))
            this.hours_electrical_consumption = value;
        else
            this.time_workload = value;
    }

    @Override
    public String toString() {
        return "Usage{" +
                "days_use_time=" + days_use_time +
                ", hours_use_time=" + hours_use_time +
                ", years_use_time=" + years_use_time +
                ", usage_location='" + usage_location + '\'' +
                ", hours_electrical_consumption=" + hours_electrical_consumption +
                ", time_workload=" + time_workload +
                '}';
    }
}