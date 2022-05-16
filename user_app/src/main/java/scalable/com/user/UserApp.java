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

  


}
