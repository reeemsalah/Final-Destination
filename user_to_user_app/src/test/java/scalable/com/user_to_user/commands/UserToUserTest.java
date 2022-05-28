package scalable.com.user_to_user.commands;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import scalable.com.shared.classes.Arango;
import scalable.com.user_to_user.UserToUserApp;

import static org.junit.Assert.*;

public class UserToUserTest {
    private static String userId1 = "1", userId2 = "2", userId3 = "3", artistName = "artist-1";
    private static Arango arango;
    @BeforeClass
    public static void setUp() {
        try {
            arango = Arango.getConnectedInstance();
            UserToUserApp app= new UserToUserApp();
            app.dbInit();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    //@AfterClass
    //public static void deleteDatabase() {
    //    arango.dropDatabase("user_to_user");
    //}

    public static String blockUser(int blocked_id) {
        JSONObject body = new JSONObject();
        body.put("blocked_id", blocked_id);

        JSONObject uriParams = new JSONObject();

        JSONObject token= new JSONObject();
        token.put("id", userId1);

        JSONObject request = new JSONObject();
        request.put("body", body);
        request.put("methodType", "POST");
        request.put("uriParams", uriParams);
        request.put("isAuthenticated",true);
        request.put("tokenPayload", token);

        PostBlockUser blockUser= new PostBlockUser();
        return blockUser.execute(request);
    }

    public static String followUser(int followed_id) {
        JSONObject body = new JSONObject();
        body.put("followed_id", followed_id);

        JSONObject uriParams = new JSONObject();

        JSONObject token= new JSONObject();
        token.put("id", userId1);

        JSONObject request = new JSONObject();
        request.put("body", body);
        request.put("methodType", "POST");
        request.put("uriParams", uriParams);
        request.put("isAuthenticated",true);
        request.put("tokenPayload", token);

        PostFollowUser followUser= new PostFollowUser();
        return followUser.execute(request);
    }

    public static String reportUser(int reported_Id, String comment) {
        JSONObject body = new JSONObject();
        body.put("reported_id", reported_Id);
        body.put("comment", comment);

        JSONObject uriParams = new JSONObject();

        JSONObject token= new JSONObject();
        token.put("id", "1");

        JSONObject request = new JSONObject();
        request.put("body", body);
        request.put("methodType", "POST");
        request.put("uriParams", uriParams);
        request.put("isAuthenticated",true);
        request.put("tokenPayload", token);

        ReportUser reportUser= new ReportUser();
        return reportUser.execute(request);
    }

    public static String getBlockedUsers() {
        GetBlockedUsers getBlockedUsers = new GetBlockedUsers();
        JSONObject body = new JSONObject();

        JSONObject token= new JSONObject();
        token.put("id", userId1);

        JSONObject uriParams = new JSONObject();

        JSONObject request = new JSONObject();
        request.put("isAuthenticated",true);
        request.put("tokenPayload", token);
        request.put("body", body);
        request.put("methodType", "GET");
        request.put("uriParams", uriParams);

        return getBlockedUsers.execute(request);
    }

    public static String getFollowedUsers() {
        GetFollowedUsers getFollowedUsers = new GetFollowedUsers();
        JSONObject body = new JSONObject();

        JSONObject token= new JSONObject();
        token.put("id", userId1);

        JSONObject uriParams = new JSONObject();
        JSONObject request = new JSONObject();
        request.put("isAuthenticated",true);
        request.put("tokenPayload", token);
        request.put("body", body);
        request.put("methodType", "GET");
        request.put("uriParams", uriParams);

        return getFollowedUsers.execute(request);
    }

    //public static String getTotalListeners(String artist) {
    //    JSONObject body = new JSONObject();
    //    body.put("artist", artist);
    //
    //    JSONObject uriParams = new JSONObject();
    //
    //    JSONObject token= new JSONObject();
    //    token.put("id", userId1);
    //
    //    JSONObject request = new JSONObject();
    //    request.put("body", body);
    //    request.put("methodType", "POST");
    //    request.put("uriParams", uriParams);
    //    request.put("isAuthenticated",true);
    //    request.put("tokenPayload", token);
    //
    //    GetTotalListeners getTotalListeners= new GetTotalListeners();
    //    return getTotalListeners.execute(request);
    //}

    @Test
    public void reportUserTest() {
        int beforeCount= arango.documentCount("user_to_user", "reports");
        String response = reportUser(200, "test comment");
        JSONObject responseJson = new JSONObject(response);
        int afterCount =arango.documentCount("user_to_user", "reports");
        assertEquals("Reported", responseJson.getString("msg"));
        assertEquals(beforeCount+1, afterCount);
    }

    @Test
    public void getBlockedUsersTest() {
        blockUser(Integer.parseInt(userId2));
        blockUser(Integer.parseInt(userId3));
        
        String response = getBlockedUsers();
        JSONObject responseJson = new JSONObject(response);
        assertEquals(200, responseJson.getInt("statusCode"));
        JSONArray dataArr = (JSONArray) (responseJson.get("data"));
        assertEquals(2, dataArr.length());
        arango.dropCollection("user_to_user", "blocked_ids");
    }

    @Test
    public void getFollowedUsersTest() {
        followUser(Integer.parseInt(userId2));
        followUser(Integer.parseInt(userId3));

        String response = getFollowedUsers();
        JSONObject responseJson = new JSONObject(response);
        assertEquals(200, responseJson.getInt("statusCode"));
        JSONArray dataArr = (JSONArray) (responseJson.get("data"));
        assertEquals(2, dataArr.length());
        arango.dropCollection("user_to_user", "followed_ids");
    }

    //@Test
    //public void getTotalListeners() {
    //    String response = getTotalListeners(artistName);
    //    JSONObject responseJson = new JSONObject(response);
    //    System.out.println(responseJson);
    //    assertNotEquals(0, responseJson.get("msg").toString().length());
    //}
}
