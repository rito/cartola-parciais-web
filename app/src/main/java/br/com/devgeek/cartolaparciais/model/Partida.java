package br.com.devgeek.cartolaparciais.model;

import android.text.TextUtils;

import br.com.devgeek.cartolaparciais.api.model.ApiPartidas_Partida;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by geovannefduarte on 18/09/17.
 */
public class Partida extends RealmObject {

    @PrimaryKey
    private long idPartida;

    private int rodada;
    private String tituloRodada;

    private String dataPartida;
    private String local;

    
    private int idTimeCasa;
    private int posicaoTimeCasa;
    private int placarTimeCasa;
    private String aproveitamentoTimeCasa;


    private int idTimeVisitante;
    private int posicaoTimeVisitante;
    private int placarTimeVisitante;
    private String aproveitamentoTimeVisitante;

    
    private String urlConfronto;
    private String urlTransmissao;
    private boolean valida;


    public Partida(){
    }


    public Partida(long idPartida, int rodada, String tituloRodada){
        this.idPartida = idPartida;
        this.rodada = rodada;
        this.tituloRodada = tituloRodada;
    }


    public Partida(int rodada, String tituloRodada, ApiPartidas_Partida partida){
        this.rodada = rodada;
        this.tituloRodada = tituloRodada;
        this.idPartida = partida.getIdPartida();
        this.dataPartida = partida.getDataPartida();
        this.local = partida.getLocal();
        this.idTimeCasa = partida.getIdTimeCasa();
        this.posicaoTimeCasa = partida.getPosicaoTimeCasa();
        this.placarTimeCasa = partida.getPlacarTimeCasa();
        this.aproveitamentoTimeCasa = TextUtils.join("_",partida.getAproveitamentoTimeCasa());
        this.idTimeVisitante = partida.getIdTimeVisitante();
        this.posicaoTimeVisitante = partida.getPosicaoTimeVisitante();
        this.placarTimeVisitante = partida.getPlacarTimeVisitante();
        this.aproveitamentoTimeVisitante = TextUtils.join("_",partida.getAproveitamentoTimeVisitante());
        this.urlConfronto = partida.getUrlConfronto();
        this.urlTransmissao = partida.getUrlTransmissao();
        this.valida = partida.isValida();
    }


    public Partida(long idPartida, int rodada, String tituloRodada, String dataPartida, String local, int idTimeCasa, int posicaoTimeCasa, int placarTimeCasa, String aproveitamentoTimeCasa, int idTimeVisitante, int posicaoTimeVisitante, int placarTimeVisitante, String aproveitamentoTimeVisitante, String urlConfronto, String urlTransmissao, boolean valida){
        this.idPartida = idPartida;
        this.rodada = rodada;
        this.tituloRodada = tituloRodada;
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
    public int getRodada(){
        return rodada;
    }
    public void setRodada(int rodada){
        this.rodada = rodada;
    }
    public String getTituloRodada(){
        return tituloRodada;
    }
    public void setTituloRodada(String tituloRodada){
        this.tituloRodada = tituloRodada;
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
    public int getPlacarTimeCasa(){
        return placarTimeCasa;
    }
    public void setPlacarTimeCasa(int placarTimeCasa){
        this.placarTimeCasa = placarTimeCasa;
    }
    public String getAproveitamentoTimeCasa(){
        return aproveitamentoTimeCasa;
    }
    public void setAproveitamentoTimeCasa(String aproveitamentoTimeCasa){
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
    public int getPlacarTimeVisitante(){
        return placarTimeVisitante;
    }
    public void setPlacarTimeVisitante(int placarTimeVisitante){
        this.placarTimeVisitante = placarTimeVisitante;
    }
    public String getAproveitamentoTimeVisitante(){
        return aproveitamentoTimeVisitante;
    }
    public void setAproveitamentoTimeVisitante(String aproveitamentoTimeVisitante){
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