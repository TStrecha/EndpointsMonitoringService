package io.dxheroes.endpointsmonitoringservice.dto;

import lombok.Data;

@Data
public class StatusDTO {

    private Status status;
    private Long timestamp;

    public StatusDTO(Status status){
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }

    public enum Status{
        SUCCESS, WARNING, FAILURE;
    }

}
