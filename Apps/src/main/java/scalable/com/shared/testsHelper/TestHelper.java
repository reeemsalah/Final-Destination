package scalable.com.shared.testsHelper;

import org.json.JSONObject;
import scalable.com.shared.App;
import scalable.com.shared.classes.Command;

import java.util.Map;
import java.util.Properties;

public class TestHelper {

    public static String execute(App app ,Command command,  JSONObject request){
       Properties properties=  app.classManager.validationMap.get(command.getCommandName());
       command.validationProperties=properties;
       return command.execute(request);
    }

}
