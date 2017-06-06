package br.com.devgeek.cartolaparciais.controller.dto;

/**
 * Created by geovannefduarte
 */
public class GreetingDTO {

    private final long id;
    private final String content;

    public GreetingDTO(long id, String content){
        this.id = id;
        this.content = content;
    }

    public long getId(){
        return id;
    }

    public String getContent(){
        return content;
    }
}