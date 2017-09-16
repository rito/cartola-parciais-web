package br.com.devgeek.cartolaparciais.api.model;

/**
 * Created by geovannefduarte on 15/09/17.
 */
public class ApiLogin {

    private String id;
    private String userMessage;
    private String glbId;


    public ApiLogin(){
    }


    public ApiLogin(String id, String userMessage, String glbId){
        this.id = id;
        this.userMessage = userMessage;
        this.glbId = glbId;
    }


    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getUserMessage(){
        return userMessage;
    }
    public void setUserMessage(String userMessage){
        this.userMessage = userMessage;
    }
    public String getGlbId(){
        return glbId;
    }
    public void setGlbId(String glbId){
        this.glbId = glbId;
    }
}