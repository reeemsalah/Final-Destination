package scalable.com.shared.classes;

import org.json.JSONObject;
import scalable.com.shared.App;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.*;

public class Controller  {

App app;

public Controller(App app){
    this.app=app;
}

public boolean isFrozen=false;
public void handleControllerMessage(JSONObject request){
       System.out.println("Mum Look I am controlling the app remotely !!!!"+request);
}
public void start(){
    ThreadPoolManager threadPool=this.app.getThreadPool();
    //TODO get  the thread count from properties
    threadPool.initThreadPool(ServiceConstants.DEFAULT_THREADS_COUNT);
    try {
        this.app.getRabbitMQCommunicatorApp().startListening();
    }
    catch (Exception e){
        System.out.println(e.getStackTrace());
        System.exit(1);
    }
}

    public void freeze() {
    
        if (isFrozen) {
            return;
        }
        try {
            this.app.getRabbitMQCommunicatorApp().pauseListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
        destroyPools();
        isFrozen = true;
    }

    
    public void resume() {
        if (!isFrozen) {
            return;
        }
        System.out.println("About to resume...");
        this.app.getThreadPool().initThreadPool(Integer.parseInt(this.app.properties.getProperty("threadsCount")));
        System.out.println("Thread pool is ready.");
        try {
            this.app.dbInit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("DB pool is ready.");
        try {
            this.app.getRabbitMQCommunicatorApp().startListening();
        } catch (IOException e) {
            e.printStackTrace();
            destroyPools();
            System.exit(1);
        }
        System.out.println("Accepting requests from queue.");
        isFrozen = false;
    }
    private void destroyPools(){
        this.app.getThreadPool().releaseThreadPool();
        if(this.app.properties.contains("arangodb")) {
            try {
                Arango.getInstance().destroyPool();
            } catch (PoolDoesNotExistException e) {
                throw new RuntimeException(e);
            }


        }
        if(this.app.properties.contains("postgres")){
            try {
                this.app.sqlDb.shutdownDriver();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    


}
