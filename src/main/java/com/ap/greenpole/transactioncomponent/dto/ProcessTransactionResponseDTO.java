package com.ap.greenpole.transactioncomponent.dto;

import java.util.ArrayList;
import java.util.List;

public class ProcessTransactionResponseDTO {

    private ProcessMasterTransactionDTO masterTransactionDTO;

    private List<ProcessMasterTransactionResponseDTO> processMasterTransactionResponseDTOList;

    private List<ProcessTransactionRecordDTO> processTransactionRecordDTOS;


    public ProcessMasterTransactionDTO getMasterTransactionDTO() {
        return masterTransactionDTO;
    }

    public void setMasterTransactionDTO(ProcessMasterTransactionDTO masterTransactionDTO) {
        this.masterTransactionDTO = masterTransactionDTO;
    }

    public List<ProcessMasterTransactionResponseDTO> getProcessMasterTransactionResponseDTOList() {
        return processMasterTransactionResponseDTOList;
    }

    public void setProcessMasterTransactionResponseDTOList(List<ProcessMasterTransactionResponseDTO> processMasterTransactionResponseDTOList) {
        this.processMasterTransactionResponseDTOList = processMasterTransactionResponseDTOList;
    }

    public List<ProcessTransactionRecordDTO> getProcessTransactionRecordDTOS() {
        return processTransactionRecordDTOS;
    }

    public void setProcessTransactionRecordDTOS(List<ProcessTransactionRecordDTO> processTransactionRecordDTOS) {
        this.processTransactionRecordDTOS = processTransactionRecordDTOS;
    }

    @Override
    public String toString() {
        return "ProcessTransactionResponseDTO{" +
                "masterTransactionDTO=" + masterTransactionDTO +
                ", processMasterTransactionResponseDTOList=" + processMasterTransactionResponseDTOList +
                ", processTransactionRecordDTOS=" + processTransactionRecordDTOS +
                '}';
    }
}
