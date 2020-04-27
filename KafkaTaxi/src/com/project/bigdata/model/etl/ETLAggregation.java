package com.project.bigdata.model.etl;

import com.project.bigdata.model.TaxiTripRecord;

public class ETLAggregation {

    private Long departuresNumber;
    private Long arrivalsNumber;
    private Long numberOfOutgoingPeople;
    private Long numberOfIncomingPeople;

    public ETLAggregation update(TaxiTripRecord record){
        if(record.getStartStop().equals("0")) {
            departuresNumber++;
            numberOfOutgoingPeople += record.getPassengerCount();
        }else if(record.getStartStop().equals("1")) {
            arrivalsNumber++;
            numberOfIncomingPeople += record.getPassengerCount();
        }
        return this;
    }

    public ETLAggregation() {
    }

    public ETLAggregation(Long departuresNumber, Long arrivalsNumber, Long numberOfOutgoingPeople, Long numberOfIncomingPeople) {
        this.departuresNumber = departuresNumber;
        this.arrivalsNumber = arrivalsNumber;
        this.numberOfOutgoingPeople = numberOfOutgoingPeople;
        this.numberOfIncomingPeople = numberOfIncomingPeople;
    }

    @Override
    public String toString() {
        return departuresNumber.toString() + " " + arrivalsNumber.toString() + " " + numberOfOutgoingPeople.toString()
                + " " + numberOfIncomingPeople.toString();
    }


    public Long getDeparturesNumber() {
        return departuresNumber;
    }

    public void setDeparturesNumber(Long departuresNumber) {
        this.departuresNumber = departuresNumber;
    }

    public Long getArrivalsNumber() {
        return arrivalsNumber;
    }

    public void setArrivalsNumber(Long arrivalsNumber) {
        this.arrivalsNumber = arrivalsNumber;
    }

    public Long getNumberOfOutgoingPeople() {
        return numberOfOutgoingPeople;
    }

    public void setNumberOfOutgoingPeople(Long numberOfOutgoingPeople) {
        this.numberOfOutgoingPeople = numberOfOutgoingPeople;
    }

    public Long getNumberOfIncomingPeople() {
        return numberOfIncomingPeople;
    }

    public void setNumberOfIncomingPeople(Long numberOfIncomingPeople) {
        this.numberOfIncomingPeople = numberOfIncomingPeople;
    }
}
