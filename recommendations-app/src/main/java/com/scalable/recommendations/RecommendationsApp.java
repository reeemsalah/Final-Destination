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
        //arangoPool =Arango.getConnectedInstance() ;
        arangoPool =Arango.getInstance() ;
        arangoPool.createCollectionIfNotExists("spotifyArangoDb","usersss",true);
        System.out.println(arangoPool.containsCollection("spotifyArangoDb","usersss"));
        app.start();

    }


    @Override
    protected String getAppName() {
        return "Recommendations";
    }
}
