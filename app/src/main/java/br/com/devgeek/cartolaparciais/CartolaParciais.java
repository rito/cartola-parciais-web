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
    protected static Long lastTimeAtualizarMercadoWasExecuted = null;
    protected static Long lastTimeAtualizarParciaisWasExecuted = null;

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

    public static boolean isTimeToAtualizarParciais(){

        try {

            Long currentTime = System.currentTimeMillis();

            if (lastTimeAtualizarParciaisWasExecuted == null || (currentTime - lastTimeAtualizarParciaisWasExecuted) > ONE_MINUTE){
                lastTimeAtualizarParciaisWasExecuted = currentTime;
                return  true;
            }

            return false;

        } catch (Exception e){

            logErrorOnConsole("isTimeToAtualizarParciais", e.getMessage(), e);
            return  true;
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(6)               // Must be bumped when the schema changes
                .migration(realmMigration())    // Migration to run instead of throwing an exception
                //.deleteRealmIfMigrationNeeded()
                .initialData(realm -> { /*realm.createObject(TimeFavorito.class); */ })
                .build();
        // Realm.deleteRealm(realmConfig);         // Delete Realm between app restarts.
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
        };
    }
}