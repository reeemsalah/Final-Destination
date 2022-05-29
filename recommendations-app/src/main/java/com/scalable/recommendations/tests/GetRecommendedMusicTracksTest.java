package com.scalable.recommendations.tests;

import com.scalable.recommendations.RecommendationsApp;
import com.scalable.recommendations.commands.GetRecommendedMusicTracks;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import scalable.com.shared.App;
import scalable.com.shared.testsHelper.TestHelper;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class GetRecommendedMusicTracksTest {
    static App appBeingTested;
    @BeforeClass
    public static void createSetup() throws TimeoutException, IOException, ClassNotFoundException {
        RecommendationsApp app = new RecommendationsApp();
        app.start();
        appBeingTested = app;
    }

    public static JSONObject RequestSimulator(){
        JSONObject body = new JSONObject();
        JSONObject request = makeRequest(null, "GET", new JSONObject());
        System.out.println("Request " + request.toString());
        GetRecommendedMusicTracks getRecommendedMusicTracks = new  GetRecommendedMusicTracks();
        return new JSONObject(TestHelper.execute(GetRecommendedMusicTracksTest.appBeingTested,getRecommendedMusicTracks,request));
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
