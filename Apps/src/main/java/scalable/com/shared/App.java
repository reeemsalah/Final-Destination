package scalable.com.shared;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Delivery;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import scalable.com.rabbitMQ.RabbitMQApp;
import scalable.com.rabbitMQ.RabbitMQCommunicatorApp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public abstract class App  {

protected Properties properties;
protected RabbitMQApp rabbitMQApp;
protected RabbitMQCommunicatorApp rabbitMQCommunicatorApp;
//read the .properties file and set the properties variable
protected abstract void getProperties();
protected abstract String getAppName();
protected void start() throws IOException, TimeoutException {
    this.getProperties();
    this.initRabbitMQ();
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
}


