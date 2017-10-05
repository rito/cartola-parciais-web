package br.com.devgeek.cartolaparciais;

import android.app.Application;

import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.RealmSchema;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.logErrorOnConsole;

/**
 * Created by geovannefduarte
 */
public class CartolaParciais extends Application {

    private final static int ONE_MINUTE = 60000;
    protected static Long lastTimeAtualizarLigasWasExecuted = null;
    protected static Long lastTimeAtualizarMercadoWasExecuted = null;
    protected static Long lastTimeAtualizarParciaisWasExecuted = null;
    protected static Long lastTimeAtualizarPartidasWasExecuted = null;

    public static boolean isTimeToAtualizarMercado(){

        try {

            Long currentTime = System.currentTimeMillis();

            if (lastTimeAtualizarMercadoWasExecuted == null || (currentTime - lastTimeAtualizarMercadoWasExecuted) > ONE_MINUTE){
                lastTimeAtualizarMercadoWasExecuted = currentTime;
                return  true;
            }

            return false;

        } catch (Exception e){

            logErrorOnConsole("isTimeToAtualizarMercado", e.getMessage(), e);
            return  true;
        }
    }

    public static boolean isTimeToUpdatePartidas(){

        try {

            Long currentTime = System.currentTimeMillis();

            if (lastTimeAtualizarPartidasWasExecuted == null || (currentTime - lastTimeAtualizarPartidasWasExecuted) > ONE_MINUTE){
                lastTimeAtualizarPartidasWasExecuted = currentTime;
                return  true;
            }

            return false;

        } catch (Exception e){

            logErrorOnConsole("isTimeToUpdatePartidas", e.getMessage(), e);
            return  true;
        }
    }

    public static boolean isTimeToUpdateParciais(){

        try {

            Long currentTime = System.currentTimeMillis();

            if (lastTimeAtualizarParciaisWasExecuted == null || (currentTime - lastTimeAtualizarParciaisWasExecuted) > ONE_MINUTE){
                lastTimeAtualizarParciaisWasExecuted = currentTime;
                return  true;
            }

            return false;

        } catch (Exception e){

            logErrorOnConsole("isTimeToUpdateParciais", e.getMessage(), e);
            return  true;
        }
    }

    public static boolean isTimeToUpdateLigas(){

        try {

            Long currentTime = System.currentTimeMillis();

            if (lastTimeAtualizarLigasWasExecuted == null || (currentTime - lastTimeAtualizarLigasWasExecuted) > ONE_MINUTE){
                lastTimeAtualizarLigasWasExecuted = currentTime;
                return  true;
            }

            return false;

        } catch (Exception e){

            logErrorOnConsole("isTimeToUpdateLigas", e.getMessage(), e);
            return  true;
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(14)               // Must be bumped when the schema changes
                .migration(realmMigration())    // Migration to run instead of throwing an exception
                //.deleteRealmIfMigrationNeeded()
                .initialData(realm -> { /*realm.createObject(TimeFavorito.class); */ })
                .build();
        //Realm.deleteRealm(realmConfig);         // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);
    }

    private final RealmMigration realmMigration(){

        return (realm, oldVersion, newVersion) -> {

            // DynamicRealm exposes an editable schema
            RealmSchema schema = realm.getSchema();

            if (oldVersion == 2){
                schema.get("AtletasPontuados").addField("rodada", Integer.class);
                oldVersion++;
            }

            if (oldVersion == 3){
                schema.get("AtletasPontuados").addField("cartoletas", Double.class);
                oldVersion++;
            }

            if (oldVersion == 4){
                schema.create("UsuarioGlobo").addField("glbId", String.class, FieldAttribute.PRIMARY_KEY);
                oldVersion++;
            }

            if (oldVersion == 5){
                schema.get("TimeFavorito")
                      .removeField("posicao")
                      .addField("timeFavorito", boolean.class)
                      .addField("timeDoUsuario", boolean.class);

                RealmResults<DynamicRealmObject> listaTimesFavoritos = realm.where("TimeFavorito").findAll();
                if (listaTimesFavoritos != null && listaTimesFavoritos.size() > 0){
                    for (DynamicRealmObject realmObject : listaTimesFavoritos){
                        realmObject.setBoolean("timeFavorito", true);
                    }
                } oldVersion++;
            }

            if (oldVersion == 6){
                schema.create("Liga")
                      .addField("ligaId", long.class, FieldAttribute.PRIMARY_KEY)
                      .addField("timeDonoId", Long.class)
                      .addField("nomeDaLiga", String.class)
                      .addField("descricaoDaLiga", String.class)
                      .addField("slug", String.class)
                      .addField("urlFlamulaPng", String.class)
                      .addField("totalTimesLiga", Long.class)
                      .addField("ranking", Long.class)
                      .addField("tipoLiga", String.class);
                oldVersion++;
            }

            if (oldVersion == 7){
                schema.create("Partida")
                      .addField("idPartida", long.class, FieldAttribute.PRIMARY_KEY)
                      .addField("rodada", int.class)
                      .addField("tituloRodada", String.class)
                      .addField("dataPartida", String.class)
                      .addField("local", String.class)

                      .addField("idTimeCasa", int.class)
                      .addField("posicaoTimeCasa", int.class)
                      .addField("placarTimeCasa", int.class)
                      .addField("aproveitamentoTimeCasa", String.class)

                      .addField("idTimeVisitante", int.class)
                      .addField("posicaoTimeVisitante", int.class)
                      .addField("placarTimeVisitante", int.class)
                      .addField("aproveitamentoTimeVisitante", String.class)

                      .addField("urlConfronto", String.class)
                      .addField("urlTransmissao", String.class)
                      .addField("valida", boolean.class);
                oldVersion++;
            }

            if (oldVersion == 8){
                schema.get("TimeFavorito")
                      .removeField("atletas");

                schema.get("TimeFavorito")
                      .addField("atletas", String.class);
                oldVersion++;
            }

            if (oldVersion == 9){
                schema.get("Partida")
                      .removeField("placarTimeCasa")
                      .removeField("placarTimeVisitante");

                schema.get("Partida")
                      .addField("placarTimeCasa", Integer.class)
                      .addField("placarTimeVisitante", Integer.class);

                RealmResults<DynamicRealmObject> listaPartidas = realm.where("Partida").findAll();
                if (listaPartidas != null && listaPartidas.size() > 0){
                    listaPartidas.deleteAllFromRealm();
                } oldVersion++;
            }

            if (oldVersion == 10){
                schema.create("TimeLiga")
                      .addField("id", String.class, FieldAttribute.PRIMARY_KEY)
                      .addField("ligaId", Long.class)
                      .addField("timeId", Long.class)
                      .addField("nomeDoTime", String.class)
                      .addField("nomeDoCartoleiro", String.class)
                      .addField("slug", String.class)
                      .addField("facebookId", Long.class)
                      .addField("urlEscudoPng", String.class)
                      .addField("urlEscudoPlaceholderPng", String.class)
                      .addField("fotoPerfil", String.class)
                      .addField("assinante", Boolean.class)
                      .addField("timeDoUsuario", boolean.class)
                      .addField("pontuacaoRodada", Double.class)
                      .addField("pontuacaoMes", Double.class)
                      .addField("pontuacaoTurno", Double.class)
                      .addField("pontuacaoCampeonato", Double.class)
                      .addField("patrimonio", Double.class)
                      .addField("atletas", String.class);
                oldVersion++;
            }

            if (oldVersion == 11){
                schema.get("MercadoStatus")
                      .removeField("temporada");

                schema.get("MercadoStatus")
                      .addField("temporada", int.class, FieldAttribute.PRIMARY_KEY);

                oldVersion++;
            }

            if (oldVersion == 12){
                schema.get("TimeLiga")
                      .addField("pontuacao", Double.class)
                      .addField("variacaoCartoletas", Double.class);

                oldVersion++;
            }

            if (oldVersion == 13){
                schema.get("TimeFavorito")
                      .addField("atletasIds", String.class);

                oldVersion++;
            }
        };
    }
}