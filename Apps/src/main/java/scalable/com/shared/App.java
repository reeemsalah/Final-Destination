package scalable.com.shared;

import com.rabbitmq.client.Delivery;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import scalable.com.Interfaces.Hook;
import scalable.com.rabbitMQ.RabbitMQApp;
import scalable.com.rabbitMQ.RabbitMQCommunicatorApp;
import scalable.com.shared.classes.ClassManager;
import scalable.com.shared.classes.Command;
import scalable.com.shared.classes.Controller;
import scalable.com.shared.classes.ThreadPoolManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public abstract class App  {

protected Properties properties;
protected RabbitMQApp rabbitMQApp;
protected RabbitMQCommunicatorApp rabbitMQCommunicatorApp;
protected Controller appController;
protected ThreadPoolManager threadsManager;
protected ClassManager classManager;

//read the .properties file and set the properties variable

protected abstract void getProperties();
protected abstract String getAppName();

protected void start() throws IOException, TimeoutException, ClassNotFoundException {
    appController=new Controller(this);
    this.getProperties();
    this.initRabbitMQ();
    this.classManager.init();
    this.threadsManager=new ThreadPoolManager();
     
    appController.start();
}
protected  void initRabbitMQ() throws IOException, TimeoutException {
    Hook hook=this::appHook;
    this.rabbitMQApp=new RabbitMQApp(properties.getProperty("rabbitMQ_host"));
    this.rabbitMQCommunicatorApp=this.rabbitMQApp.getNewCommunicator(this.getAppName().toUpperCase()+"App",hook);
    
}
public void appHook(String consumerTag, Delivery delivery) throws IOException {
  //this method is called whenever this app's rabbitmq receives a message
   

    String response = "";
    try {
        // invoking command
        String req = new String(delivery.getBody(), StandardCharsets.UTF_8);

        System.out.println("App: request: " + req);

        // TODO change command name
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject propertiesJson = (org.json.simple.JSONObject) parser.parse(req);
        Object commandName = propertiesJson.get("functionName");

        //response += executor.apply((String) commandName, new JSONObject(req));

        System.out.println("Sending: " + response);
    } catch (ParseException e) {
        e.printStackTrace();
    } finally {
        // sending response message to the response queue, which is defined by the
        // request message's properties
             rabbitMQCommunicatorApp.sendResponse(delivery,response);

    }
}

    // function to invoke the required command using command pattern design
    public String invokeCommand(String commandName, JSONObject req) {
        final Callable<String> callable = () -> runCommand(commandName, req);

        // submitting the callback fn. to the thread pool
        Future<String> executorCallable = threadsManager.submit(callable);
        //here they are blocking ?? why !
        // waiting until the future is done
        while (!executorCallable.isDone()) ;

        // returning the output of the future
        try {
            return executorCallable.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "{\"statusCode\": 507, \"msg\": \"Execution was interrupted\"}";
        } catch (ExecutionException e) {
            return "{\"statusCode\": 521, \"msg\": \"Execution threw an exception\"}";
        }
    }

    private String runCommand(String functionName, JSONObject req) {
        try {
            // getting the class responsible for the command
            // TODO get this from a classManager
            
            final Class<?> commandClass = classManager.getCommand(functionName);
            // creating an instance of the command class
            final Command commandInstance = (Command) commandClass.getDeclaredConstructor().newInstance();
            // callback responsible for invoking the required method of the command class
            return (String) commandClass.getMethod("execute", req.getClass()).invoke(commandInstance, req);
        } catch (ClassNotFoundException e) {
            return "{\"statusCode\": 404, \"msg\": \"Function-Name class: (" + functionName + ") not found\"}";
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            return "{\"statusCode\": 504, \"msg\": \"Function-Name class: not operational\"}";
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return "{\"statusCode\": 510, \"msg\": \"Function-Name class: threw an exception\"}";
        }
    }
public  ThreadPoolManager getThreadPool(){
    return threadsManager;
}
}


