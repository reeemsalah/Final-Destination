package scalable.com.shared;

import scalable.com.rabbitMQ.RabbitMQApp;
import scalable.com.rabbitMQ.RabbitMQCommunicatorApp;

import java.io.IOException;
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
    this.rabbitMQApp=new RabbitMQApp(properties.getProperty("rabbitMQ_host"));
    this.rabbitMQCommunicatorApp=this.rabbitMQApp.getNewCommunicator(this.getAppName().toUpperCase()+"App");
    
}

}
