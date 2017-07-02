package br.com.devgeek.cartolaparciais.api;

import java.io.Serializable;

/**
 * Created by geovannefduarte
 */

public class ApiReturn implements Serializable {

    private static final long serialVersionUID = 1L;

    private int statusCode;
    private String response;


    public ApiReturn(){
        super();
    }


    public ApiReturn(int statusCode){
        super();
        this.statusCode = statusCode;
    }


    public ApiReturn(int statusCode, String response){
        super();
        this.statusCode = statusCode;
        this.response = response;
    }


    public int getStatusCode(){
        return statusCode;
    }
    public void setStatusCode(int statusCode){
        this.statusCode = statusCode;
    }
    public String getResponse(){
        return response;
    }
    public void setResponse(String response){
        this.response = response;
    }
}