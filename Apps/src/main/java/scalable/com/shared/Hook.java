package scalable.com.shared;


import com.rabbitmq.client.Delivery;

import java.io.IOException;

public interface Hook {
public void  send(String consumerTag, Delivery delivery) throws IOException;

}
