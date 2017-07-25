package br.com.devgeek.cartolaparciais.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by geovannefduarte
 */
public class ApiMercadoStatus {

    @Expose
    @SerializedName("rodada_atual")
    private int rodadaAtual;

    @Expose
    @SerializedName("status_mercado")
    private int statusDoMercado;

    @Expose
    @SerializedName("esquema_default_id")
    private int esquemaDefaultId;

    @Expose
    @SerializedName("cartoleta_inicial")
    private int cartoletaInicial;

    @Expose
    @SerializedName("max_ligas_free")
    private int maximoLigas_free;

    @Expose
    @SerializedName("max_ligas_pro")
    private int maximoLigas_pro;

    @Expose
    @SerializedName("max_ligas_matamata_free")
    private int maximoLigasMatamata_free;

    @Expose
    @SerializedName("max_ligas_matamata_pro")
    private int maximoLigasMatamata_pro;

    @Expose
    @SerializedName("max_ligas_patrocinadas_free")
    private int maximoLigasPatrocinadas_free;

    @Expose
    @SerializedName("max_ligas_patrocinadas_pro_num")
    private int maximoLigasPatrocinadas_pro_num;

    @Expose
    @SerializedName("game_over")
    private boolean gameOver;

    @Expose
    @SerializedName("temporada")
    private int temporada;

    @Expose
    @SerializedName("reativar")
    private boolean reativar;

    @Expose
    @SerializedName("times_escalados")
    private long timesEscalados;

    @Expose
    @SerializedName("fechamento")
    private ApiMercadoStatusFechamento fechamento;

    @Expose
    @SerializedName("mercado_pos_rodada")
    private boolean mercadoPosRodada;

    @Expose
    @SerializedName("aviso")
    private String aviso;


    public ApiMercadoStatus(){
    }


    public ApiMercadoStatus(int rodadaAtual, int statusDoMercado, int esquemaDefaultId, int cartoletaInicial, int maximoLigas_free, int maximoLigas_pro, int maximoLigasMatamata_free, int maximoLigasMatamata_pro, int maximoLigasPatrocinadas_free, int maximoLigasPatrocinadas_pro_num, boolean gameOver, int temporada, boolean reativar, long timesEscalados, ApiMercadoStatusFechamento fechamento, boolean mercadoPosRodada, String aviso){
        this.rodadaAtual = rodadaAtual;
        this.statusDoMercado = statusDoMercado;
        this.esquemaDefaultId = esquemaDefaultId;
        this.cartoletaInicial = cartoletaInicial;
        this.maximoLigas_free = maximoLigas_free;
        this.maximoLigas_pro = maximoLigas_pro;
        this.maximoLigasMatamata_free = maximoLigasMatamata_free;
        this.maximoLigasMatamata_pro = maximoLigasMatamata_pro;
        this.maximoLigasPatrocinadas_free = maximoLigasPatrocinadas_free;
        this.maximoLigasPatrocinadas_pro_num = maximoLigasPatrocinadas_pro_num;
        this.gameOver = gameOver;
        this.temporada = temporada;
        this.reativar = reativar;
        this.timesEscalados = timesEscalados;
        this.fechamento = fechamento;
        this.mercadoPosRodada = mercadoPosRodada;
        this.aviso = aviso;
    }


    public int getRodadaAtual(){
        return rodadaAtual;
    }
    public void setRodadaAtual(int rodadaAtual){
        this.rodadaAtual = rodadaAtual;
    }
    public int getStatusDoMercado(){
        return statusDoMercado;
    }
    public void setStatusDoMercado(int statusDoMercado){
        this.statusDoMercado = statusDoMercado;
    }
    public int getEsquemaDefaultId(){
        return esquemaDefaultId;
    }
    public void setEsquemaDefaultId(int esquemaDefaultId){
        this.esquemaDefaultId = esquemaDefaultId;
    }
    public int getCartoletaInicial(){
        return cartoletaInicial;
    }
    public void setCartoletaInicial(int cartoletaInicial){
        this.cartoletaInicial = cartoletaInicial;
    }
    public int getMaximoLigas_free(){
        return maximoLigas_free;
    }
    public void setMaximoLigas_free(int maximoLigas_free){
        this.maximoLigas_free = maximoLigas_free;
    }
    public int getMaximoLigas_pro(){
        return maximoLigas_pro;
    }
    public void setMaximoLigas_pro(int maximoLigas_pro){
        this.maximoLigas_pro = maximoLigas_pro;
    }
    public int getMaximoLigasMatamata_free(){
        return maximoLigasMatamata_free;
    }
    public void setMaximoLigasMatamata_free(int maximoLigasMatamata_free){
        this.maximoLigasMatamata_free = maximoLigasMatamata_free;
    }
    public int getMaximoLigasMatamata_pro(){
        return maximoLigasMatamata_pro;
    }
    public void setMaximoLigasMatamata_pro(int maximoLigasMatamata_pro){
        this.maximoLigasMatamata_pro = maximoLigasMatamata_pro;
    }
    public int getMaximoLigasPatrocinadas_free(){
        return maximoLigasPatrocinadas_free;
    }
    public void setMaximoLigasPatrocinadas_free(int maximoLigasPatrocinadas_free){
        this.maximoLigasPatrocinadas_free = maximoLigasPatrocinadas_free;
    }
    public int getMaximoLigasPatrocinadas_pro_num(){
        return maximoLigasPatrocinadas_pro_num;
    }
    public void setMaximoLigasPatrocinadas_pro_num(int maximoLigasPatrocinadas_pro_num){
        this.maximoLigasPatrocinadas_pro_num = maximoLigasPatrocinadas_pro_num;
    }
    public boolean isGameOver(){
        return gameOver;
    }
    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }
    public int getTemporada(){
        return temporada;
    }
    public void setTemporada(int temporada){
        this.temporada = temporada;
    }
    public boolean isReativar(){
        return reativar;
    }
    public void setReativar(boolean reativar){
        this.reativar = reativar;
    }
    public long getTimesEscalados(){
        return timesEscalados;
    }
    public void setTimesEscalados(long timesEscalados){
        this.timesEscalados = timesEscalados;
    }
    public ApiMercadoStatusFechamento getFechamento(){
        return fechamento;
    }
    public void setFechamento(ApiMercadoStatusFechamento fechamento){
        this.fechamento = fechamento;
    }
    public boolean isMercadoPosRodada(){
        return mercadoPosRodada;
    }
    public void setMercadoPosRodada(boolean mercadoPosRodada){
        this.mercadoPosRodada = mercadoPosRodada;
    }
    public String getAviso(){
        return aviso;
    }
    public void setAviso(String aviso){
        this.aviso = aviso;
    }
}