package scalable.com.user_to_user;

import scalable.com.databaseHelper.DatabaseHelper;
import scalable.com.shared.App;
import scalable.com.shared.classes.ClassManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class UserToUserApp extends App{

    public static void main(String[] args){

        UserToUserApp app = new UserToUserApp();
//        app.dbInit();
//        app.start();
//        //comment this line after development
//        DatabaseHelper.createSchema();
//        DatabaseHelper.createProcs();
    }

    @Override
    protected String getAppName() {
        return "UserToUser";
    }
}
