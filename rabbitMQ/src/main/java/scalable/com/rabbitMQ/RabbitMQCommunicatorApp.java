package scalable.com.rabbitMQ;

import com.rabbitmq.client.Channel;

import java.io.IOException;

public class RabbitMQCommunicatorApp extends RabbitMQCommunicator {
    private String queueName;
    private String consumerTag;
      public RabbitMQCommunicatorApp(String queueName,Channel channel) throws IOException {
          super(channel);
          this.queueName=queueName;
          this.declareSpecificQueue(queueName);
      }


}
