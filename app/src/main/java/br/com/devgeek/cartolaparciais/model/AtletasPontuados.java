package br.com.devgeek.cartolaparciais.model;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import br.com.devgeek.cartolaparciais.api.model.ApiAtletasMercado_PontuacaoAtleta;
import br.com.devgeek.cartolaparciais.api.model.ApiAtletasPontuados_PontuacaoAtleta;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.logErrorOnConsole;

/**
 * Created by geovannefduarte
 */
public class AtletasPontuados extends RealmObject {

    private static final String TAG = "AtletasPontuados";

    @PrimaryKey
    private String atletaId;

    private Integer rodada;

    private String apelido;
    private Double pontuacao;
    private Double cartoletas;
    private RealmList<Scouts> scouts;

    private String foto;
    private Integer posicaoId;
    private Integer clubeId;


    public AtletasPontuados(){
    }


    public AtletasPontuados(Integer rodada, ApiAtletasMercado_PontuacaoAtleta pontuacaoAtleta){
        this.rodada = rodada;
        this.atletaId = String.valueOf(pontuacaoAtleta.getAtleta_id());

        this.apelido = pontuacaoAtleta.getApelido();
        this.pontuacao = pontuacaoAtleta.getPontuacao();
        this.cartoletas = pontuacaoAtleta.getCartoletas();
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


    public AtletasPontuados(String atletaId, Integer rodada, ApiAtletasPontuados_PontuacaoAtleta pontuacaoAtleta){
        this.atletaId = atletaId;
        this.rodada = rodada;
        this.apelido = pontuacaoAtleta.getApelido();
        this.pontuacao = pontuacaoAtleta.getPontuacao();
        this.cartoletas = null;
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


    public AtletasPontuados(String atletaId, Integer rodada, String apelido, Double pontuacao, Double cartoletas, HashMap<String, Integer> scouts, String foto, Integer posicaoId, Integer clubeId){
        this.atletaId = atletaId;
        this.rodada = rodada;
        this.apelido = apelido;
        this.pontuacao = pontuacao;
        this.cartoletas = cartoletas;
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


    public static void mergeAtletasPontuados(Realm realm, AtletasPontuados master, AtletasPontuados slave){

        try {
            master.setApelido(slave.getApelido());
            master.setPontuacao(slave.getPontuacao());
            master.setCartoletas(slave.getCartoletas());
            master.setFoto(slave.getFoto());
            master.setPosicaoId(slave.getPosicaoId());
            master.setClubeId(slave.getClubeId());

            RealmList<Scouts> scouts = new RealmList<>();

            if (slave.getScouts() != null){
                scouts = slave.getScouts();

                if (!scouts.isManaged()){ // if the 'list' is managed, all items in it is also managed
                    RealmList<Scouts> managedImageList = new RealmList<>();
                    for (Scouts scout : scouts) {
                        if (scout.isManaged()) {
                            managedImageList.add(scout);
                        } else {
                            managedImageList.add(realm.copyToRealm(scout));
                        }
                    }
                    scouts = managedImageList;
                }
            }

            master.setScouts(scouts);
        } catch (Exception e){
            logErrorOnConsole(TAG, "mergeAtletasPontuados() -> "+e.getMessage(), e);
        }
    }


    public String getAtletaId(){
        return atletaId;
    }
    public void setAtletaId(String atletaId){
        this.atletaId = atletaId;
    }
    public Integer getRodada(){
        return rodada;
    }
    public void setRodada(Integer rodada){
        this.rodada = rodada;
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
    public Double getCartoletas(){
        return cartoletas;
    }
    public void setCartoletas(Double cartoletas){
        this.cartoletas = cartoletas;
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