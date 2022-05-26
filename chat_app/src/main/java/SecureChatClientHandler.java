import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Handles a client-side channel.
 */
public class SecureChatClientHandler extends SimpleChannelInboundHandler<String> {
    private String clientUsername;
    private BufferedReader bufferedReader;

//    public void SecureChatServerHandler() throws IOException {
//            this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//            this.clientUsername = bufferedReader.readLine();
//            System.out.println("User " + clientUsername + "has entered tha chat");
//
//    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        
        System.err.println(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
