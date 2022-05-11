package scalable.com.user;

import scalable.com.shared.App;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class UserApp extends App {

        public static void main(String[] args) throws TimeoutException, IOException, ClassNotFoundException {
            new UserApp().start();
        }


    @Override
    protected String getAppName() {
        return "User";
    }
}
