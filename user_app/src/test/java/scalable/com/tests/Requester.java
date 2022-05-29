package scalable.com.tests;

import org.json.JSONObject;
import scalable.com.shared.testsHelper.TestHelper;
import scalable.com.user.UserApp;
import scalable.com.user.commands.*;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Requester {
    Requester() {
    
    }

    //static JSONObject authenticationParams = AuthParamsHandler.getUnauthorizedAuthParams();

    private static JSONObject makeRequest(JSONObject body, String methodType, JSONObject uriParams,boolean needsTokenPayload) {
        JSONObject request = new JSONObject();
        request.put("body", body);
        request.put("methodType", methodType);
        request.put("uriParams", uriParams);
        
        if(!needsTokenPayload) {
            JSONObject tokenPayload=new JSONObject();
            tokenPayload.put("id",UserAppTest.id);
            tokenPayload.put("isArtist",UserAppTest.isArtist);
           
            request.put("tokenPayload",tokenPayload );
        }
        return request;
    }

    public static JSONObject callSignUpCommand(String username, String email, String password,String firstName,String lastName,boolean isArtist) {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("email", email);
        body.put("password", password);
        body.put("firstname",firstName);
        body.put("lastname",lastName);
        body.put("isArtist",isArtist);
        JSONObject request = makeRequest(body, "POST", new JSONObject(),false);
        SignUp signUp=new SignUp();
        return new JSONObject(TestHelper.execute(signUp,request));
    }

    public static JSONObject login(String email, String password) {
        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("password", password);
        JSONObject request = makeRequest(body, "POST", new JSONObject(),false);
        Login login=new Login();
        return new JSONObject(TestHelper.execute(login,request));
    }

    public static JSONObject callViewMyProfileCommand() {
        JSONObject request = makeRequest(new JSONObject(), "GET", new JSONObject(),true);
        TestHelper.attachTokenPayLoad(UserAppTest.token,request);
        ViewMyProfile viewMyProfile=new ViewMyProfile();
        return new JSONObject(TestHelper.execute(viewMyProfile,request));
    }

    public static JSONObject viewAnotherProfile(String username) {
        JSONObject uriParams = new JSONObject().put("username", username);
        JSONObject request = makeRequest(null, "GET", uriParams,true);
        return new JSONObject(new ViewAnotherUserProfile().execute(request));
    }

    public static JSONObject updatePassword(String oldPassword, String newPassword) {
        JSONObject body = new JSONObject();
        body.put("oldPassword", oldPassword);
        body.put("newPassword", newPassword);
        JSONObject request = makeRequest(body, "POST", new JSONObject(),true);
       ChangePassword changePassword= new ChangePassword();
        return new JSONObject(TestHelper.execute(changePassword,request));
    }

    static public JSONObject updateProfilePicture() throws IOException {
        JSONObject request = makeRequest(new JSONObject(), "PUT", new JSONObject(),true);
        //JSONObject files = new JSONObject().put("image", org.sab.minio.FileSimulation.generateImageJson());
        //request.put("files", files);
        return new JSONObject(new EditProfilePicture().execute(request));
    }

  

    public static JSONObject deleteAccount(String password) {
        JSONObject body = new JSONObject();
        body.put("password", password);

        JSONObject request = makeRequest(body, "DELETE", new JSONObject(),true);
        return new JSONObject(new DeleteAccount().execute(request));
    }


}
