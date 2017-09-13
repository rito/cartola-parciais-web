package br.com.devgeek.cartolaparciais.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by geovannefduarte on 12/09/17.
 */
public class ApiAtletasMercado implements Serializable {

    private static String URL = "https://api.cartolafc.globo.com/atletas/mercado";

    private List<ApiAtletasMercado_PontuacaoAtleta> atletas;


    public ApiAtletasMercado(){
    }


    public ApiAtletasMercado(List<ApiAtletasMercado_PontuacaoAtleta> atletas){
        this.atletas = atletas;
    }


    public List<ApiAtletasMercado_PontuacaoAtleta> getAtletas(){
        return atletas;
    }
    public void setAtletas(List<ApiAtletasMercado_PontuacaoAtleta> atletas){
        this.atletas = atletas;
    }
}