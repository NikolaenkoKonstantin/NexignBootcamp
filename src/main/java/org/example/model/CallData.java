package org.example.model;

import org.joda.time.DateTime;

import java.time.LocalDateTime;

public class CallData {
    private String phoneNumber;
    private String callType;
    private LocalDateTime dateTimeCallStart;
    private LocalDateTime dateTimeCallEnd;
    private String fareType;


    public CallData(){}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
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

    @Override
    public String toString() {
        return "CallData{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", callType='" + callType + '\'' +
                ", dateTimeCallStart=" + dateTimeCallStart +
                ", dateTimeCallEnd=" + dateTimeCallEnd +
                ", fareType='" + fareType + '\'' +
                '}';
    }
}
