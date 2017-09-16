package br.com.devgeek.cartolaparciais.api.model;

/**
 * Created by geovannefduarte on 15/09/17.
 */
public class ApiLogin_Payload {

    private String email;
    private String password;
    private int serviceId;


    public ApiLogin_Payload(){
    }


    public ApiLogin_Payload(String email, String password, int serviceId){
        this.email = email;
        this.password = password;
        this.serviceId = serviceId;
    }


    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public int getServiceId(){
        return serviceId;
    }
    public void setServiceId(int serviceId){
        this.serviceId = serviceId;
    }
}