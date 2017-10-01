package br.com.devgeek.cartolaparciais.model;

import br.com.devgeek.cartolaparciais.api.model.ApiAuthLigaSlug_Time;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by geovannefduarte
 */
public class TimeLiga extends RealmObject {

    public static String FORMATO_PONTUACAO = "#0.00";
    public static String FORMATO_CARTOLETAS = "#0.00";

    @PrimaryKey
    private String id;
    private Long ligaId;
    private Long timeId;

    private String nomeDoTime;
    private String nomeDoCartoleiro;
    private String slug;
    private Long facebookId;
    private String urlEscudoPng;
    private String urlEscudoPlaceholderPng;
    private String fotoPerfil;
    private Boolean assinante;
    private boolean timeDoUsuario;

    private Double pontuacaoRodada;
    private Double pontuacaoMes;
    private Double pontuacaoTurno;
    private Double pontuacaoCampeonato;
    private Double patrimonio;
    private String atletas;


    public TimeLiga(){
    }


    public TimeLiga(Long ligaId, ApiAuthLigaSlug_Time time, Double pontuacaoRodada, boolean timeDoUsuario){

        this.id = String.valueOf(ligaId+time.getTimeId());
        this.ligaId = ligaId;
        this.timeId = time.getTimeId();
        this.nomeDoTime = time.getNomeDoTime();
        this.nomeDoCartoleiro = time.getNomeDoCartoleiro();
        this.slug = time.getSlug();
        this.facebookId = time.getFacebookId();
        this.urlEscudoPng = time.getUrlEscudoPng();
        this.urlEscudoPlaceholderPng = time.getUrlEscudoPlaceholderPng();
        this.fotoPerfil = time.getFotoPerfil();
        this.assinante = time.getAssinante();
        this.timeDoUsuario = timeDoUsuario;
        this.patrimonio = time.getPatrimonio();
        if (time.getPontos() != null){
            this.pontuacaoMes = time.getPontos().getMes();
            this.pontuacaoTurno = time.getPontos().getTurno();
            this.pontuacaoCampeonato = time.getPontos().getCampeonato();

            if (pontuacaoRodada != null){
                this.pontuacaoRodada = pontuacaoRodada;
            } else {
                this.pontuacaoRodada = time.getPontos().getRodada();
            }
        }
    }


    public TimeLiga(String id, Long ligaId, Long timeId, String nomeDoTime, String nomeDoCartoleiro, String slug, Long facebookId, String urlEscudoPng, String urlEscudoPlaceholderPng, String fotoPerfil, Boolean assinante, boolean timeDoUsuario, Double pontuacaoRodada, Double pontuacaoMes, Double pontuacaoTurno, Double pontuacaoCampeonato, Double patrimonio, String atletas){
        this.id = id;
        this.ligaId = ligaId;
        this.timeId = timeId;
        this.nomeDoTime = nomeDoTime;
        this.nomeDoCartoleiro = nomeDoCartoleiro;
        this.slug = slug;
        this.facebookId = facebookId;
        this.urlEscudoPng = urlEscudoPng;
        this.urlEscudoPlaceholderPng = urlEscudoPlaceholderPng;
        this.fotoPerfil = fotoPerfil;
        this.assinante = assinante;
        this.timeDoUsuario = timeDoUsuario;
        this.pontuacaoRodada = pontuacaoRodada;
        this.pontuacaoMes = pontuacaoMes;
        this.pontuacaoTurno = pontuacaoTurno;
        this.pontuacaoCampeonato = pontuacaoCampeonato;
        this.patrimonio = patrimonio;
        this.atletas = atletas;
    }


    public static void mergeTimesDaLiga(TimeLiga master, TimeLiga slave){
        master.setNomeDoTime(slave.getNomeDoTime());
        master.setNomeDoCartoleiro(slave.getNomeDoCartoleiro());
        master.setSlug(slave.getSlug());
        master.setFacebookId(slave.getFacebookId());
        master.setUrlEscudoPng(slave.getUrlEscudoPng());
        master.setUrlEscudoPlaceholderPng(slave.getUrlEscudoPlaceholderPng());
        master.setFotoPerfil(slave.getFotoPerfil());
        master.setAssinante(slave.getAssinante());
        master.setTimeDoUsuario(slave.isTimeDoUsuario());
        master.setPontuacaoRodada(slave.getPontuacaoRodada());
        master.setPontuacaoMes(slave.getPontuacaoMes());
        master.setPontuacaoTurno(slave.getPontuacaoTurno());
        master.setPontuacaoCampeonato(slave.getPontuacaoCampeonato());
        master.setPatrimonio(slave.getPatrimonio());
        master.setAtletas(slave.getAtletas());
    }


    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public Long getLigaId(){
        return ligaId;
    }
    public void setLigaId(Long ligaId){
        this.ligaId = ligaId;
    }
    public Long getTimeId(){
        return timeId;
    }
    public void setTimeId(Long timeId){
        this.timeId = timeId;
    }
    public String getNomeDoTime(){
        return nomeDoTime;
    }
    public void setNomeDoTime(String nomeDoTime){
        this.nomeDoTime = nomeDoTime;
    }
    public String getNomeDoCartoleiro(){
        return nomeDoCartoleiro;
    }
    public void setNomeDoCartoleiro(String nomeDoCartoleiro){
        this.nomeDoCartoleiro = nomeDoCartoleiro;
    }
    public String getSlug(){
        return slug;
    }
    public void setSlug(String slug){
        this.slug = slug;
    }
    public Long getFacebookId(){
        return facebookId;
    }
    public void setFacebookId(Long facebookId){
        this.facebookId = facebookId;
    }
    public String getUrlEscudoPng(){
        return urlEscudoPng;
    }
    public void setUrlEscudoPng(String urlEscudoPng){
        this.urlEscudoPng = urlEscudoPng;
    }
    public String getUrlEscudoPlaceholderPng(){
        return urlEscudoPlaceholderPng;
    }
    public void setUrlEscudoPlaceholderPng(String urlEscudoPlaceholderPng){
        this.urlEscudoPlaceholderPng = urlEscudoPlaceholderPng;
    }
    public String getFotoPerfil(){
        return fotoPerfil;
    }
    public void setFotoPerfil(String fotoPerfil){
        this.fotoPerfil = fotoPerfil;
    }
    public Boolean getAssinante(){
        return assinante;
    }
    public void setAssinante(Boolean assinante){
        this.assinante = assinante;
    }
    public boolean isTimeDoUsuario(){
        return timeDoUsuario;
    }
    public void setTimeDoUsuario(boolean timeDoUsuario){
        this.timeDoUsuario = timeDoUsuario;
    }
    public Double getPontuacaoRodada(){
        return pontuacaoRodada;
    }
    public void setPontuacaoRodada(Double pontuacaoRodada){
        this.pontuacaoRodada = pontuacaoRodada;
    }
    public Double getPontuacaoMes(){
        return pontuacaoMes;
    }
    public void setPontuacaoMes(Double pontuacaoMes){
        this.pontuacaoMes = pontuacaoMes;
    }
    public Double getPontuacaoTurno(){
        return pontuacaoTurno;
    }
    public void setPontuacaoTurno(Double pontuacaoTurno){
        this.pontuacaoTurno = pontuacaoTurno;
    }
    public Double getPontuacaoCampeonato(){
        return pontuacaoCampeonato;
    }
    public void setPontuacaoCampeonato(Double pontuacaoCampeonato){
        this.pontuacaoCampeonato = pontuacaoCampeonato;
    }
    public Double getPatrimonio(){
        return patrimonio;
    }
    public void setPatrimonio(Double patrimonio){
        this.patrimonio = patrimonio;
    }
    public String getAtletas(){
        return atletas;
    }
    public void setAtletas(String atletas){
        this.atletas = atletas;
    }
}