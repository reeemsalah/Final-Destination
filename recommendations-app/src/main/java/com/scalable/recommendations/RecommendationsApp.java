package com.scalable.recommendations;

import scalable.com.shared.App;
import scalable.com.shared.classes.Arango;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public  class RecommendationsApp extends App {
    public static Arango arangoPool;
    public static void main(String[] args)throws TimeoutException, IOException, ClassNotFoundException, SQLException {
        RecommendationsApp app=new RecommendationsApp();
        arangoPool = new Arango();
        app.dbInit();
        app.start();

    }
    @Override
    protected void dbInit() throws IOException {
        Arango arango = Arango.getInstance();
        arango.createPool(15);
        arango.createDatabaseIfNotExists("Spotify");
        arango.createCollectionIfNotExists("Spotify","users",true);
        arango.createCollectionIfNotExists("Spotify","music_tracks",true);

    }

    @Override
    protected String getAppName() {
        return "Recommendations";
    }
}
