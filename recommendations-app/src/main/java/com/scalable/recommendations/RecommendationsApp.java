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
        app.dbInit();
        arangoPool =Arango.getInstance() ;
        System.out.println(arangoPool.databaseExists("spotifyArangoDB"));
        arangoPool.createCollectionIfNotExists("spotifyArangoDb","users",false);

//        arangoPool.createCollectionIfNotExists("spotifyArangoDb","users",true);
//        System.out.println(arangoPool.containsCollection("spotifyArangoDb","users"));
        arangoPool =Arango.getConnectedInstance() ;
        arangoPool.createCollectionIfNotExists("spotifyArangoDb","usersss",true);
        System.out.println(arangoPool.containsCollection("spotifyArangoDb","usersss"));
        app.start();

    }


    @Override
    protected String getAppName() {
        return "Recommendations";
    }
}
