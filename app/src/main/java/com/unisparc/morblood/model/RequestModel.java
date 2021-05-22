package com.unisparc.morblood.model;

import java.io.Serializable;

public class RequestModel implements Serializable {
    private String donorId;
    private String recipientId;
    private  Long timestamp;
    private Boolean accept;

    public RequestModel(){
        // empty constructor required
    }

    public RequestModel(String donorId, String recipientId, Long timestamp, Boolean accept){
        this.donorId = donorId;
        this.recipientId = recipientId;
        this.timestamp = timestamp;
        this.accept = accept;
    }

    public String getDonorId() {return donorId; }

    public String getRecipientId() {return recipientId;}

    public Long getTimestamp() {return timestamp;}

    public Boolean getAccept(){return accept;}
}
