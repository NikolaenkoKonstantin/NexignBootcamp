package org.example.model;

import java.time.LocalDateTime;

public class CallDataRecord {
    private long phoneNumber;
    private String callType;
    private LocalDateTime dateTimeCallStart;
    private LocalDateTime dateTimeCallEnd;
    private String fareType;


    public CallDataRecord(){}


    public CallDataRecord(long phoneNumber, String callType, LocalDateTime dateTimeCallStart,
                          LocalDateTime dateTimeCallEnd, String fareType) {
        this.phoneNumber = phoneNumber;
        this.callType = callType;
        this.dateTimeCallStart = dateTimeCallStart;
        this.dateTimeCallEnd = dateTimeCallEnd;
        this.fareType = fareType;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public LocalDateTime getDateTimeCallStart() {
        return dateTimeCallStart;
    }

    public void setDateTimeCallStart(LocalDateTime dateTimeCallStart) {
        this.dateTimeCallStart = dateTimeCallStart;
    }

    public LocalDateTime getDateTimeCallEnd() {
        return dateTimeCallEnd;
    }

    public void setDateTimeCallEnd(LocalDateTime dateTimeCallEnd) {
        this.dateTimeCallEnd = dateTimeCallEnd;
    }

    public String getFareType() {
        return fareType;
    }

    public void setFareType(String fareType) {
        this.fareType = fareType;
    }
}
