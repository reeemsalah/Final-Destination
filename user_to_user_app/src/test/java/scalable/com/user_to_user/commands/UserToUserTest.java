package scalable.com.user_to_user.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import scalable.com.shared.classes.Arango;
import scalable.com.user_to_user.UserToUserApp;

public class UserToUserTest {
    private static int userId1 = 1, userId2 = 2, userId3 = 3;
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

    @AfterClass
    public static void deleteDatabase() {
        arango.dropDatabase("user_to_user");
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

        JSONObject uriParams = new JSONObject();
        JSONObject request = new JSONObject();
        request.put("body", body);
        request.put("methodType", "GET");
        request.put("uriParams", uriParams);

        return getBlockedUsers.execute(request);
    }

    public static String getFollowedUsers() {
        GetFollowedUsers getFollowedUsers = new GetFollowedUsers();
        JSONObject body = new JSONObject();

        JSONObject uriParams = new JSONObject();
        JSONObject request = new JSONObject();
        request.put("body", body);
        request.put("methodType", "GET");
        request.put("uriParams", uriParams);

        return getFollowedUsers.execute(request);
    }

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
        // userBlockUser(userId1, userId2);
        // userBlockUser(userId1, userId3);
        String response = getBlockedUsers();
        JSONObject responseJson = new JSONObject(response);
        assertEquals(200, responseJson.getInt("statusCode"));
        JSONArray dataArr = (JSONArray) (responseJson.get("data"));
        assertEquals(2, dataArr.length());
        arango.dropCollection("user_to_user", "blocked_ids");
    }

    @Test
    public void getFollowedUsersTest() {
        // userBlockUser(userId1, userId2);
        // userBlockUser(userId1, userId3);
        String response = getFollowedUsers();
        JSONObject responseJson = new JSONObject(response);
        assertEquals(200, responseJson.getInt("statusCode"));
        JSONArray dataArr = (JSONArray) (responseJson.get("data"));
        assertEquals(2, dataArr.length());
        arango.dropCollection("user_to_user", "followed_ids");
    }

}
