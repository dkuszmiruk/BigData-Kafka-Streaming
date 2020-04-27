package com.project.bigdata.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaxiTripRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger("TaxiTrip");
    private String tripId;
    private String startStop;//maybe Integer
    private String timestamp;
    private Long locationId;
    private Long passengerCount;
    private Double trip_distance;
    private String payment_type;
    private String amount;
    private String vendorId;

    public TaxiTripRecord(String tripId, String startStop, String timestamp, Long locationId, Long passengerCount, Double trip_distance, String payment_type, String amount, String vendorId) {
        this.tripId = tripId;
        this.startStop = startStop;
        this.timestamp = timestamp;
        this.locationId = locationId;
        this.passengerCount = passengerCount;
        this.trip_distance = trip_distance;
        this.payment_type = payment_type;
        this.amount = amount;
        this.vendorId = vendorId;
    }

    public TaxiTripRecord() {
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getStartStop() {
        return startStop;
    }

    public void setStartStop(String startStop) {
        this.startStop = startStop;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(Long passengerCount) {
        this.passengerCount = passengerCount;
    }

    public Double getTrip_distance() {
        return trip_distance;
    }

    public void setTrip_distance(Double trip_distance) {
        this.trip_distance = trip_distance;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public static TaxiTripRecord parseFromCSVLine(String line){
        if(line.isEmpty()){
            logger.log(Level.ALL,"Line is empty");
        }
        String[] record = line.split(",");
        return new TaxiTripRecord(record[0], record[1], record[2], Long.parseLong(record[3]), Long.parseLong(record[4]),
                Double.parseDouble(record[5]), record[6], record[7],record[8]);
    }

    @JsonIgnore
    public long getTimestampInMillis(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        Date date;
        try{
            date = simpleDateFormat.parse(this.timestamp);
//            date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
            return date.getTime();
        } catch (ParseException e){
            return -1;
        }
    }

    @JsonIgnore
    public Date getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try{
//            date = simpleDateFormat.parse(this.timestamp);
//            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            return simpleDateFormat1.parse(simpleDateFormat1.format(date));
            return simpleDateFormat1.parse(simpleDateFormat1.format(simpleDateFormat.parse(this.timestamp)));
        } catch (ParseException e){
            logger.log(Level.WARNING,"Problem with date parse");
            return null;
        }
    }
}
