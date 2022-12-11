package fr.univpau.dudesalonso.boaviztapp.serverconfig.usage;

public class Usage{
    public int days_use_time;
    public int hours_use_time;
    public int years_use_time;
    public String usage_location;
    public int hours_electrical_consumption;

    public Usage(int days_use_time, int hours_use_time, int years_use_time, String usage_location, int hours_electrical_consumption) {
        this.days_use_time = days_use_time;
        this.hours_use_time = hours_use_time;
        this.years_use_time = years_use_time;
        this.usage_location = usage_location;
        this.hours_electrical_consumption = hours_electrical_consumption;
    }
}