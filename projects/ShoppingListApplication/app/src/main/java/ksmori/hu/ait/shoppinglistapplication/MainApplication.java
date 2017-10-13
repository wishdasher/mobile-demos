package ksmori.hu.ait.shoppinglistapplication;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {

    private Realm realmItem;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        context = this;
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realmItem = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmItem.close();
    }

    public Realm getRealmItem() {
        return realmItem;
    }

    public static Context getContext(){
        return context;
    }
}
