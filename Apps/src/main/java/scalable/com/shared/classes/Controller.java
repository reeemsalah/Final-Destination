package scalable.com.shared.classes;

import org.json.JSONObject;
import scalable.com.shared.App;
import scalable.com.shared.AppsConstants;

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
    threadPool.initThreadPool(Integer.parseInt(this.app.properties.getProperty(AppsConstants.DEFAULT_MAX_Threads_PROPERTY_NAME)));
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
        this.app.getThreadPool().initThreadPool(Integer.parseInt(this.app.properties.getProperty(AppsConstants.DEFAULT_MAX_Threads_PROPERTY_NAME)));
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
    public void setMaxThreadsCount(int maxThreadsCount) throws IOException {
        System.out.println("setting threads count to: "+maxThreadsCount);
        this.app.properties.setProperty(AppsConstants.DEFAULT_MAX_Threads_PROPERTY_NAME,""+maxThreadsCount);
        try {
            reloadThreadPool();
        } catch (IOException e) {
            e.printStackTrace();
            destroyResourcesAndExit();
        }
        System.out.println("successfully set the threads count");
    }
    public void setMaxDbConnectionsCount(int maxDbConnectionsCount) throws IOException {
        System.out.println("setting max db connections count"+maxDbConnectionsCount);
        if(this.app.properties.contains("arangodb")) {
            this.app.properties.put(AppsConstants.DEFAULT_NUMBER_OF_ARANGO_DB_PROPERTY_Name,""+maxDbConnectionsCount);
        }
        if(this.app.properties.contains("postgres")){
            this.app.properties.put(AppsConstants.DEFAULT_NUMBER_OF_POSTGRES_DB_PROPERTY_Name,""+maxDbConnectionsCount);
        }

        try {
            reloadDBPools();
        } catch (IOException e) {
            e.printStackTrace();
            destroyResourcesAndExit();
        }
        System.out.println("successfully set max db connections count");
    }

    private void destroyPools(){
    System.out.println("destroying pools");
        this.app.getThreadPool().releaseThreadPool();
        this.destroyDBPools();
        System.out.println("successfully destroyed pools");
     
    }
    private void destroyDBPools(){
    System.out.println("Destroying DB pools");
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
        System.out.println("successfully destroyed db pools");
    }
    private void destroyResourcesAndExit() throws IOException {
        this.app.getRabbitMQCommunicatorApp().pauseListening();
        destroyPools();
       
        System.exit(1);
    }
 
    private void reloadThreadPool() throws IOException {
        System.out.println("Reloading threads pools");
    this.app.getRabbitMQCommunicatorApp().pauseListening();

        this.app.getThreadPool().releaseThreadPool();
        this.app.getThreadPool().initThreadPool(Integer.parseInt(this.app.properties.getProperty(AppsConstants.DEFAULT_MAX_Threads_PROPERTY_NAME)));
        this.app.getRabbitMQCommunicatorApp().startListening();
    }

    private void reloadDBPools() throws IOException {
    System.out.println("Reloading Db pools");
    this.app.getRabbitMQCommunicatorApp().pauseListening();
    this.destroyDBPools();
    this.app.dbInit();
    this.app.getRabbitMQCommunicatorApp().startListening();
    }



}
