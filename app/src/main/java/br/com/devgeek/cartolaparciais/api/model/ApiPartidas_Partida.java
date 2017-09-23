package br.com.devgeek.cartolaparciais.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by geovannefduarte on 18/09/17.
 */
public class ApiPartidas_Partida {

    @SerializedName("partida_id")
    private long idPartida;

    @SerializedName("partida_data")
    private String dataPartida;

    @SerializedName("local")
    private String local;


    @SerializedName("clube_casa_id")
    private int idTimeCasa;

    @SerializedName("clube_casa_posicao")
    private int posicaoTimeCasa;

    @SerializedName("placar_oficial_mandante")
    private Integer placarTimeCasa;

    @SerializedName("aproveitamento_mandante")
    private ArrayList<String> aproveitamentoTimeCasa;


    @SerializedName("clube_visitante_id")
    private int idTimeVisitante;

    @SerializedName("clube_visitante_posicao")
    private int posicaoTimeVisitante;

    @SerializedName("placar_oficial_visitante")
    private Integer placarTimeVisitante;

    @SerializedName("aproveitamento_visitante")
    private ArrayList<String> aproveitamentoTimeVisitante;


    @SerializedName("url_confronto")
    private String urlConfronto;

    @SerializedName("url_transmissao")
    private String urlTransmissao;

    @SerializedName("valida")
    private boolean valida;


    public ApiPartidas_Partida(){
    }


    public ApiPartidas_Partida(long idPartida, String dataPartida, String local, int idTimeCasa, int posicaoTimeCasa, Integer placarTimeCasa, ArrayList<String> aproveitamentoTimeCasa, int idTimeVisitante, int posicaoTimeVisitante, Integer placarTimeVisitante, ArrayList<String> aproveitamentoTimeVisitante, String urlConfronto, String urlTransmissao, boolean valida){
        this.idPartida = idPartida;
        this.dataPartida = dataPartida;
        this.local = local;
        this.idTimeCasa = idTimeCasa;
        this.posicaoTimeCasa = posicaoTimeCasa;
        this.placarTimeCasa = placarTimeCasa;
        this.aproveitamentoTimeCasa = aproveitamentoTimeCasa;
        this.idTimeVisitante = idTimeVisitante;
        this.posicaoTimeVisitante = posicaoTimeVisitante;
        this.placarTimeVisitante = placarTimeVisitante;
        this.aproveitamentoTimeVisitante = aproveitamentoTimeVisitante;
        this.urlConfronto = urlConfronto;
        this.urlTransmissao = urlTransmissao;
        this.valida = valida;
    }


    public long getIdPartida(){
        return idPartida;
    }
    public void setIdPartida(long idPartida){
        this.idPartida = idPartida;
    }
    public String getDataPartida(){
        return dataPartida;
    }
    public void setDataPartida(String dataPartida){
        this.dataPartida = dataPartida;
    }
    public String getLocal(){
        return local;
    }
    public void setLocal(String local){
        this.local = local;
    }
    public int getIdTimeCasa(){
        return idTimeCasa;
    }
    public void setIdTimeCasa(int idTimeCasa){
        this.idTimeCasa = idTimeCasa;
    }
    public int getPosicaoTimeCasa(){
        return posicaoTimeCasa;
    }
    public void setPosicaoTimeCasa(int posicaoTimeCasa){
        this.posicaoTimeCasa = posicaoTimeCasa;
    }
    public Integer getPlacarTimeCasa(){
        return placarTimeCasa;
    }
    public void setPlacarTimeCasa(Integer placarTimeCasa){
        this.placarTimeCasa = placarTimeCasa;
    }
    public ArrayList<String> getAproveitamentoTimeCasa(){
        return aproveitamentoTimeCasa;
    }
    public void setAproveitamentoTimeCasa(ArrayList<String> aproveitamentoTimeCasa){
        this.aproveitamentoTimeCasa = aproveitamentoTimeCasa;
    }
    public int getIdTimeVisitante(){
        return idTimeVisitante;
    }
    public void setIdTimeVisitante(int idTimeVisitante){
        this.idTimeVisitante = idTimeVisitante;
    }
    public int getPosicaoTimeVisitante(){
        return posicaoTimeVisitante;
    }
    public void setPosicaoTimeVisitante(int posicaoTimeVisitante){
        this.posicaoTimeVisitante = posicaoTimeVisitante;
    }
    public Integer getPlacarTimeVisitante(){
        return placarTimeVisitante;
    }
    public void setPlacarTimeVisitante(Integer placarTimeVisitante){
        this.placarTimeVisitante = placarTimeVisitante;
    }
    public ArrayList<String> getAproveitamentoTimeVisitante(){
        return aproveitamentoTimeVisitante;
    }
    public void setAproveitamentoTimeVisitante(ArrayList<String> aproveitamentoTimeVisitante){
        this.aproveitamentoTimeVisitante = aproveitamentoTimeVisitante;
    }
    public String getUrlConfronto(){
        return urlConfronto;
    }
    public void setUrlConfronto(String urlConfronto){
        this.urlConfronto = urlConfronto;
    }
    public String getUrlTransmissao(){
        return urlTransmissao;
    }
    public void setUrlTransmissao(String urlTransmissao){
        this.urlTransmissao = urlTransmissao;
    }
    public boolean isValida(){
        return valida;
    }
    public void setValida(boolean valida){
        this.valida = valida;
    }
}