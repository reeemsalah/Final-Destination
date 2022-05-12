package scalable.com.user;

import scalable.com.shared.App;
import scalable.com.shared.classes.ClassManager;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class UserApp extends App {

        public static void main(String[] args) throws TimeoutException, IOException, ClassNotFoundException {


            UserApp app=new UserApp();

            app.start();
        }
      

    @Override
    protected String getAppName() {
        return "User";
    }

    @Override
    protected void initProperties() {
        Properties properties=new Properties();
        try {
            properties.load(UserApp.class.getClassLoader().getResourceAsStream("app.properties"));
            this.properties=properties;
            
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    @Override
    protected void initClassManager() {
        ClassManager classManager=new ClassManager();
        try {
            classManager.init();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
