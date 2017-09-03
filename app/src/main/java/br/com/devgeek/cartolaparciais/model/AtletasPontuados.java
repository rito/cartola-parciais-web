package br.com.devgeek.cartolaparciais.model;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import br.com.devgeek.cartolaparciais.api.model.ApiAtletasPontuados_PontuacaoAtleta;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by geovannefduarte
 */
public class AtletasPontuados extends RealmObject {

    @PrimaryKey
    private String atletaId;

    private String apelido;
    private Double pontuacao;
    private RealmList<Scouts> scouts;

    private String foto;
    private Integer posicaoId;
    private Integer clubeId;


    public AtletasPontuados(){
    }


    public AtletasPontuados(String atletaId, ApiAtletasPontuados_PontuacaoAtleta pontuacaoAtleta){
        this.atletaId = atletaId;
        this.apelido = pontuacaoAtleta.getApelido();
        this.pontuacao = pontuacaoAtleta.getPontuacao();
        this.foto = pontuacaoAtleta.getFoto();
        this.posicaoId = pontuacaoAtleta.getPosicao_id();
        this.clubeId = pontuacaoAtleta.getClube_id();

        this.scouts = new RealmList<>();
        if (pontuacaoAtleta.getScout() != null && pontuacaoAtleta.getScout().size() > 0){

            for (Map.Entry<String, Integer> entry : pontuacaoAtleta.getScout().entrySet()){

                if (entry.getKey() != null && !entry.getKey().toString().equals("") && entry.getValue() != null){

                    try {

                        int quantidade = Integer.valueOf(entry.getValue().toString());
                        this.scouts.add(new Scouts(entry.getKey().toString(), quantidade));

                    } catch (Exception e){
                        Log.e("AtletasPontuados", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public AtletasPontuados(String atletaId, String apelido, Double pontuacao, HashMap<String, Integer> scouts, String foto, Integer posicaoId, Integer clubeId){
        this.atletaId = atletaId;
        this.apelido = apelido;
        this.pontuacao = pontuacao;
        this.foto = foto;
        this.posicaoId = posicaoId;
        this.clubeId = clubeId;

        this.scouts = new RealmList<>();
        if (scouts != null && scouts.size() > 0){

            for (Map.Entry<String, Integer> entry : scouts.entrySet()){

                if (entry.getKey() != null && !entry.getKey().toString().equals("") && entry.getValue() != null){

                    try {

                        int quantidade = Integer.valueOf(entry.getValue().toString());
                        this.scouts.add(new Scouts(entry.getKey().toString(), quantidade));

                    } catch (Exception e){
                        Log.e("AtletasPontuados", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public String getAtletaId(){
        return atletaId;
    }
    public void setAtletaId(String atletaId){
        this.atletaId = atletaId;
    }
    public String getApelido(){
        return apelido;
    }
    public void setApelido(String apelido){
        this.apelido = apelido;
    }
    public Double getPontuacao(){
        return pontuacao;
    }
    public void setPontuacao(Double pontuacao){
        this.pontuacao = pontuacao;
    }
    public RealmList<Scouts> getScouts(){
        return scouts;
    }
    public void setScouts(RealmList<Scouts> scouts){
        this.scouts = scouts;
    }
    public String getFoto(){
        return foto;
    }
    public void setFoto(String foto){
        this.foto = foto;
    }
    public Integer getPosicaoId(){
        return posicaoId;
    }
    public void setPosicaoId(Integer posicaoId){
        this.posicaoId = posicaoId;
    }
    public Integer getClubeId(){
        return clubeId;
    }
    public void setClubeId(Integer clubeId){
        this.clubeId = clubeId;
    }
}