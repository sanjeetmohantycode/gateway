package com.iot.meter.gateway.dto;

public class UserDTO {

    String userId;
    String orgId;

    public UserDTO() {
    }

    public UserDTO(String userId, String orgId) {
        this.userId = userId;
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
