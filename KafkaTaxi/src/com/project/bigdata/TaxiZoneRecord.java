package com.project.bigdata;

public class TaxiZoneRecord {

    private String borough;
    private String zone;
    private String serviceZone;

    public TaxiZoneRecord(String borough, String zone, String serviceZone) {
        this.borough = borough;
        this.zone = zone;
        this.serviceZone = serviceZone;
    }

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getServiceZone() {
        return serviceZone;
    }

    public void setServiceZone(String serviceZone) {
        this.serviceZone = serviceZone;
    }
}
