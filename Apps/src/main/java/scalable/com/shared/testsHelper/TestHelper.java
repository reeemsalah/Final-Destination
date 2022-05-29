package scalable.com.shared.testsHelper;

import org.json.JSONObject;
import scalable.com.shared.App;
import scalable.com.shared.classes.Command;
import scalable.com.shared.classes.JWTHandler;

import java.util.Map;
import java.util.Properties;

public class TestHelper {
    public static App appBeingTested;
    public static String execute(App app ,Command command,  JSONObject request){
        if (!request.has(JWTHandler.IS_AUTHENTICATED)){
             request.put(JWTHandler.IS_AUTHENTICATED,false);
        }
       Properties properties=  app.classManager.validationMap.get(command.getCommandName());
       command.validationProperties=properties;
       return command.execute(request);
    }
    public static void attachTokenPayLoad(String token,JSONObject request){
          
          Object tokenPayload=JWTHandler.decodeToken(token);
          request.put(JWTHandler.TOKEN_PAYLOAD,tokenPayload);
          request.put(JWTHandler.IS_AUTHENTICATED,true);
    }

}
