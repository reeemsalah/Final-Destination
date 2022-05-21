import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public final class SecureChatClient {

    static final String HOST = System.getProperty("host", "0.0.0.0");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
    private static String clientUsername;
    private static Channel ch ;

    public SecureChatClient(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public static void main(String[] args) throws Exception {
        // tale user nickname
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter nickname :)");
        if(scanner.hasNext()){
            clientUsername =     scanner.nextLine();
            System.out.println("Welcome "+ clientUsername + " :D");
        }




    }


    public static String execute() throws IOException, InterruptedException {


        // Configure SSL.
        final SslContext sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SecureChatClientInitializer(sslCtx));

            // Start the connection attempt.
            ch = b.connect(HOST, PORT).sync().channel();


//            while(scanner.hasNext()){
//                String input = scanner.nextLine();
//                ch.writeAndFlush(
//                        "User " + clientUsername);
//
//


        } finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }

        // Read commands from the stdin.
        ChannelFuture lastWriteFuture = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (;;) {
            String line = in.readLine();
            if (line == null) {
                break;
            }

            // Sends the received line to the server.
            lastWriteFuture = ch.writeAndFlush(clientUsername +": " +line + "\r\n");

            // If user typed the 'bye' command, wait until the server closes
            // the connection.
            if ("bye".equals(line.toLowerCase())) {
                ch.closeFuture().sync();
                lastWriteFuture = ch.writeAndFlush(clientUsername +" left chat "  + "\r\n");
                break;
            }
        }

        // Wait until all messages are flushed before closing the channel.
        if (lastWriteFuture != null) {
            lastWriteFuture.sync();
        }


        return "";

    }

}







