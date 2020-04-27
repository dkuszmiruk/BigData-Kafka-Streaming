package com.project.bigdata.model.anomaly;

import com.project.bigdata.model.TaxiTripRecord;

public class AnomalyAggregation {
    private Long numberOfOutgoingPeople;
    private Long numberOfIncomingPeople;
    private Long difference;

    public AnomalyAggregation update(TaxiTripRecord record){
        if(record.getStartStop().equals("0")) {
            numberOfOutgoingPeople += record.getPassengerCount();
            difference += record.getPassengerCount();
        }else if(record.getStartStop().equals("1")) {
            numberOfIncomingPeople += record.getPassengerCount();
            difference -= record.getPassengerCount();
        }
        //It means that reduced number of people in district will be marked as positive number, above the same
//        difference = numberOfOutgoingPeople - numberOfIncomingPeople;
        return this;
    }

    public AnomalyAggregation(Long numberOfOutgoingPeople, Long numberOfIncomingPeople, Long difference) {
        this.numberOfOutgoingPeople = numberOfOutgoingPeople;
        this.numberOfIncomingPeople = numberOfIncomingPeople;
        this.difference = difference;
    }

    public AnomalyAggregation() {
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

    public Long getDifference() {
        return difference;
    }

    public void setDifference(Long difference) {
        this.difference = difference;
    }
}
