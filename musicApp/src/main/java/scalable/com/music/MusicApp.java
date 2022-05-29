package scalable.com.music;

import scalable.com.databaseHelper.DatabaseHelper;
import scalable.com.shared.App;
import scalable.com.shared.classes.Arango;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class MusicApp extends App {
    public static Arango arangoPool;

    public static void main(String[] args) throws TimeoutException, IOException, ClassNotFoundException, SQLException {
        MusicApp app=new MusicApp();
<<<<<<< HEAD
        //app.dbInit();
=======
>>>>>>> 9bac178 (Create Song: uploading song left)
        arangoPool = new Arango();
        app.dbInit();
        app.start();
        //comment this line after development
        //DatabaseHelper.createSchema();
        //DatabaseHelper.createProcs();


    }

    @Override
    public void dbInit() throws IOException {
        Arango arango = Arango.getInstance();
        arango.createPool(15);
        arango.createDatabaseIfNotExists("Spotify");
        arango.createCollectionIfNotExists("Spotify","Songs",false);

    }


    @Override
    protected String getAppName() {
        return "Music";
    }



}
