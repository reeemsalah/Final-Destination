package scalable.com.shared.classes;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.json.JSONObject;



@ChannelHandler.Sharable
public class BackdoorServerHandler extends ChannelInboundHandlerAdapter {
    private final Controller controller;

    public
    BackdoorServerHandler(Controller controller) {
        this.controller = controller;
    }

    private String toString(Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        StringBuilder sb = new StringBuilder();
        while (buf.isReadable()) {
            sb.append((char) buf.readByte());
        }
        return sb.toString();

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { //
        String controllerCmd = toString(msg);
        JSONObject req=new JSONObject();
        req.put("cmd",controllerCmd);
        controller.handleControllerMessage(req);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
