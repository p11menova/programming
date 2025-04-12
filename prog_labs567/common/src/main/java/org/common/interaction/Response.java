package org.common.interaction;

import java.io.Serializable;

public class Response implements Serializable {
    private Status responseStatus;
    private String responseBody;
    public Response(Status responseStatus, String responseBody){
        this.responseStatus = responseStatus;
        this.responseBody = responseBody;
    }
    public Response(Status responseStatus){
        this.responseStatus = responseStatus;
        this.responseBody = null;
    }

    public Status getResponseStatus() {
        return responseStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
