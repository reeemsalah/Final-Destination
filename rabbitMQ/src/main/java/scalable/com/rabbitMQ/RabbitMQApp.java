package scalable.com.rabbitMQ;

import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQApp extends RabbitMQ implements  AutoCloseable{
    private Connection connection;

    public RabbitMQApp(String host) throws IOException, TimeoutException {
        this.connection=this.createConnection(host);
    }
    public RabbitMQCommunicatorApp getNewCommunicator(String queueName) throws IOException {
        return  new RabbitMQCommunicatorApp(queueName,this.connection.createChannel());
    }

    public void close() throws IOException {
        connection.close();
    }
}
