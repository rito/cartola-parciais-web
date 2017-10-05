package br.com.devgeek.cartolaparciais.model;

import br.com.devgeek.cartolaparciais.api.model.ApiMercadoStatus;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by geovannefduarte
 */
public class MercadoStatus extends RealmObject {

    public static final int ABERTO      = 1;
    public static final int FECHADO     = 2;
    public static final int MANUTENCAO  = 4;

    @PrimaryKey
    private int temporada;
    private int rodadaAtual;
    private int statusDoMercado;
    private long timesEscalados;

    private int esquemaDefaultId;
    private int cartoletaInicial;

    private boolean gameOver;
    private boolean reativar;

    private boolean mercadoPosRodada;

    private String aviso;


    private int dia;
    private int mes;
    private int ano;
    private int hora;
    private int minuto;
    private long timestamp;


    public MercadoStatus(){
    }


    public MercadoStatus(ApiMercadoStatus apiMercadoStatus){
        this.temporada = apiMercadoStatus.getTemporada();
        this.rodadaAtual = apiMercadoStatus.getRodadaAtual();
        this.statusDoMercado = apiMercadoStatus.getStatusDoMercado();
        this.timesEscalados = apiMercadoStatus.getTimesEscalados();
        this.esquemaDefaultId = apiMercadoStatus.getEsquemaDefaultId();
        this.cartoletaInicial = apiMercadoStatus.getCartoletaInicial();
        this.gameOver = apiMercadoStatus.isGameOver();
        this.reativar = apiMercadoStatus.isReativar();
        this.mercadoPosRodada = apiMercadoStatus.isMercadoPosRodada();
        this.aviso = apiMercadoStatus.getAviso();

        this.dia = apiMercadoStatus.getFechamento().getDia();
        this.mes = apiMercadoStatus.getFechamento().getMes();
        this.ano = apiMercadoStatus.getFechamento().getAno();
        this.hora = apiMercadoStatus.getFechamento().getHora();
        this.minuto = apiMercadoStatus.getFechamento().getMinuto();
        this.timestamp = apiMercadoStatus.getFechamento().getTimestamp();
    }


    public MercadoStatus(int temporada, int rodadaAtual, int statusDoMercado, long timesEscalados, int esquemaDefaultId, int cartoletaInicial, boolean gameOver, boolean reativar, boolean mercadoPosRodada, String aviso, int dia, int mes, int ano, int hora, int minuto, long timestamp){
        this.temporada = temporada;
        this.rodadaAtual = rodadaAtual;
        this.statusDoMercado = statusDoMercado;
        this.timesEscalados = timesEscalados;
        this.esquemaDefaultId = esquemaDefaultId;
        this.cartoletaInicial = cartoletaInicial;
        this.gameOver = gameOver;
        this.reativar = reativar;
        this.mercadoPosRodada = mercadoPosRodada;
        this.aviso = aviso;
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.hora = hora;
        this.minuto = minuto;
        this.timestamp = timestamp;
    }

    public static boolean mergeMercadoStatus(MercadoStatus master, MercadoStatus slave){

        boolean hasChanges = false;

        if (slave.getRodadaAtual()      != slave.getRodadaAtual()){      master.setRodadaAtual(slave.getRodadaAtual());           hasChanges = true; }
        if (slave.getStatusDoMercado()  != slave.getStatusDoMercado()){  master.setStatusDoMercado(slave.getStatusDoMercado());   hasChanges = true; }
        if (slave.getTimesEscalados()   != slave.getTimesEscalados()){   master.setTimesEscalados(slave.getTimesEscalados());     hasChanges = true; }
        if (slave.getEsquemaDefaultId() != slave.getEsquemaDefaultId()){ master.setEsquemaDefaultId(slave.getEsquemaDefaultId()); hasChanges = true; }
        if (slave.getCartoletaInicial() != slave.getCartoletaInicial()){ master.setCartoletaInicial(slave.getCartoletaInicial()); hasChanges = true; }
        if (slave.isGameOver()          != slave.isGameOver()){          master.setGameOver(slave.isGameOver());                  hasChanges = true; }
        if (slave.isReativar()          != slave.isReativar()){          master.setReativar(slave.isReativar());                  hasChanges = true; }
        if (slave.isMercadoPosRodada()  != slave.isMercadoPosRodada()){  master.setMercadoPosRodada(slave.isMercadoPosRodada());  hasChanges = true; }
        if (slave.getAviso()            != slave.getAviso()){            master.setAviso(slave.getAviso());                       hasChanges = true; }
        if (slave.getDia()              != slave.getDia()){              master.setDia(slave.getDia());                           hasChanges = true; }
        if (slave.getMes()              != slave.getMes()){              master.setMes(slave.getMes());                           hasChanges = true; }
        if (slave.getAno()              != slave.getAno()){              master.setAno(slave.getAno());                           hasChanges = true; }
        if (slave.getHora()             != slave.getHora()){             master.setHora(slave.getHora());                         hasChanges = true; }
        if (slave.getMinuto()           != slave.getMinuto()){           master.setMinuto(slave.getMinuto());                     hasChanges = true; }
        if (slave.getTimestamp()        != slave.getTimestamp()){        master.setTimestamp(slave.getTimestamp());               hasChanges = true; }

        return hasChanges;
    }

    public boolean equals(MercadoStatus mercadoStatus){
        return  this.temporada == mercadoStatus.getTemporada() &&
                this.rodadaAtual == mercadoStatus.getRodadaAtual() &&
                this.statusDoMercado == mercadoStatus.getStatusDoMercado() &&
                this.timesEscalados == mercadoStatus.getTimesEscalados() &&
                this.esquemaDefaultId == mercadoStatus.getEsquemaDefaultId() &&
                this.cartoletaInicial == mercadoStatus.getCartoletaInicial() &&
                this.gameOver == mercadoStatus.isGameOver() &&
                this.reativar == mercadoStatus.isReativar() &&
                this.mercadoPosRodada == mercadoStatus.isMercadoPosRodada() &&

                this.aviso.equals(mercadoStatus.getAviso()) &&

                this.dia == mercadoStatus.getDia() &&
                this.mes == mercadoStatus.getMes() &&
                this.ano == mercadoStatus.getAno() &&
                this.hora == mercadoStatus.getHora() &&
                this.minuto == mercadoStatus.getMinuto() &&
                this.timestamp == mercadoStatus.getTimestamp();
    }

    public int getTemporada(){
        return temporada;
    }
    public void setTemporada(int temporada){
        this.temporada = temporada;
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
    public long getTimesEscalados(){
        return timesEscalados;
    }
    public void setTimesEscalados(long timesEscalados){
        this.timesEscalados = timesEscalados;
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
    public boolean isGameOver(){
        return gameOver;
    }
    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }
    public boolean isReativar(){
        return reativar;
    }
    public void setReativar(boolean reativar){
        this.reativar = reativar;
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
    public int getDia(){
        return dia;
    }
    public void setDia(int dia){
        this.dia = dia;
    }
    public int getMes(){
        return mes;
    }
    public void setMes(int mes){
        this.mes = mes;
    }
    public int getAno(){
        return ano;
    }
    public void setAno(int ano){
        this.ano = ano;
    }
    public int getHora(){
        return hora;
    }
    public void setHora(int hora){
        this.hora = hora;
    }
    public int getMinuto(){
        return minuto;
    }
    public void setMinuto(int minuto){
        this.minuto = minuto;
    }
    public long getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }
}