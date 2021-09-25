package com.ap.greenpole.transactioncomponent.response;

import java.io.Serializable;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 7:07 PM
 */
public class BaseResponse<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String status;

    private String statusMessage;

    private Long count;

    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMesssage) {
        this.statusMessage = statusMesssage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
}
