package com.scalable.recommendations.tests;

import com.arangodb.entity.BaseDocument;
import com.scalable.recommendations.RecommendationsApp;
import com.scalable.recommendations.commands.GetRecommendedMusicTracks;
import com.scalable.recommendations.constants.DatabaseConstants;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import scalable.com.shared.App;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.testsHelper.TestHelper;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class GetRecommendedMusicTracksTest {
    static String[] users;
    static Arango arango;
    @BeforeClass
    public static void createSetup() throws TimeoutException, IOException, ClassNotFoundException {
        RecommendationsApp app = new RecommendationsApp();
        app.start();
        TestHelper.appBeingTested=app;

        arango = Arango.getConnectedInstance();
        String DB_NAME = DatabaseConstants.DATABASE_NAME;
        String USER_COLLECTION =  DatabaseConstants.USER_DOCUMENT_COLLECTION;
        // Dummy Data
        users = new String[5];
        arango.createDatabaseIfNotExists(DB_NAME);

        //create Test user Documents
        for (int i = 0; i < users.length; i++) {
            //if document exists in collection, remove, and re-add
            //
            BaseDocument user = new BaseDocument();
            
        }
    }

    public static JSONObject RequestSimulator(){
        JSONObject body = new JSONObject();
        JSONObject request = makeRequest(null, "GET", new JSONObject());
        System.out.println("Request " + request.toString());
        GetRecommendedMusicTracks getRecommendedMusicTracks = new  GetRecommendedMusicTracks();
        return new JSONObject(TestHelper.execute(getRecommendedMusicTracks,request));
    }
    public static JSONObject makeRequest(JSONObject body, String methodType, JSONObject uriParams) {
        JSONObject request = new JSONObject();
        request.put("body", body);
        request.put("methodType", methodType);
        request.put("uriParams", uriParams);
        JSONObject authenticationParams = new JSONObject();
        authenticationParams.put("isAuthenticated", false);
        request.put("authenticationParams", authenticationParams);
        return request;
    }
}
