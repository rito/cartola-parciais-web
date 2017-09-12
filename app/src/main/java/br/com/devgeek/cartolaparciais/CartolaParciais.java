package br.com.devgeek.cartolaparciais;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by geovannefduarte
 */
public class CartolaParciais extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(3)               // Must be bumped when the schema changes
                .migration(realmMigration())    // Migration to run instead of throwing an exception
//                .deleteRealmIfMigrationNeeded()
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