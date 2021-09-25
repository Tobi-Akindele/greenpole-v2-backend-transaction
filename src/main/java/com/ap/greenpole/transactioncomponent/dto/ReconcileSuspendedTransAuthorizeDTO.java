package com.ap.greenpole.transactioncomponent.dto;

import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 11/14/2020 12:08 AM
 */
public class ReconcileSuspendedTransAuthorizeDTO {

    private List<String> cscsTransId;

    //reconcile or cancel
    private String action;

    public List<String> getCscsTransId() {
        return cscsTransId;
    }

    public void setCscsTransId(List<String> cscsTransId) {
        this.cscsTransId = cscsTransId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "ReconcileSuspendedTransAuthorizeDTO{" +
                "cscsTransId=" + cscsTransId +
                ", action='" + action + '\'' +
                '}';
    }
}
