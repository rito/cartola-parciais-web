package br.com.devgeek.cartolaparciais;

import android.app.Application;

import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
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
                .schemaVersion(5)               // Must be bumped when the schema changes
                .migration(realmMigration())    // Migration to run instead of throwing an exception
                //.deleteRealmIfMigrationNeeded()
                .initialData(realm -> { /*realm.createObject(TimeFavorito.class); */ })
                .build();
//        Realm.deleteRealm(realmConfig);         // Delete Realm between app restarts.
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
            }

//                // Migrate to version 1: Add a new class.
//                // Example:
//                // public Person extends RealmObject {
//                //     private String name;
//                //     private int age;
//                //     // getters and setters left out for brevity
//                // }
//                if (oldVersion == 2){
//                    schema.create("Person")
//                            .addField("name", String.class)
//                            .addField("age", int.class);
//                    oldVersion++;
//                }
//
//                // Migrate to version 2: Add a primary key + object references
//                // Example:
//                // public Person extends RealmObject {
//                //     private String name;
//                //     @PrimaryKey
//                //     private int age;
//                //     private Dog favoriteDog;
//                //     private RealmList<Dog> dogs;
//                //     // getters and setters left out for brevity
//                // }
//                if (oldVersion == 1){
//                    schema.get("Person")
//                            .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
//                            .addRealmObjectField("favoriteDog", schema.get("Dog"))
//                            .addRealmListField("dogs", schema.get("Dog"));
//                    oldVersion++;
//                }
        };
    }
}