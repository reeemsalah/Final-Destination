package scalable.com.shared.classes;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.json.JSONObject;
import scalable.com.rabbitMQ.RabbitMQCommunicatorServer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class Command {
     public abstract String getCommandName();
    public abstract String execute(JSONObject request);


    
}
