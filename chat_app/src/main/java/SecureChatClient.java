import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.example.telnet.TelnetClient;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public final class SecureChatClient {

    static final String HOST = System.getProperty("host", "0.0.0.0");
    int PORT = Integer.parseInt(System.getProperty("port", "4040"));
    public  String clientUsername;
    Quickstart qs;

    public SecureChatClient(String clientUsername, int port) throws IOException {
        this.clientUsername = clientUsername;
        Quickstart qs = new Quickstart();
        this.qs = qs;
        this.PORT = Integer.parseInt(System.getProperty("port", port+""));
    }

    public static void main(String[] args) throws Exception {
        //THIS IS A SIMULATION TO WHAT SHOULD HAPPEN WHEN A CLIENT INSTANCE IS CREATED
        String username = "Ahmed";
        int port = 4040;
        SecureChatClient client = new SecureChatClient(username, port);
        client.execute();


    }


    public void execute() throws Exception
    {
        // tale user nickname
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter nickname :)");
        if(scanner.hasNext()){
            clientUsername =     scanner.nextLine();
            System.out.println("Welcome "+ clientUsername + " :D");
        }

        // Configure SSL.
        final SslContext sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SecureChatClientInitializer(sslCtx,this.PORT));

            // Start the connection attempt.
            Channel ch = b.connect(HOST, PORT).sync().channel();

//            while(scanner.hasNext()){
//                String input = scanner.nextLine();
//                ch.writeAndFlush(
//                        "User " + clientUsername);
//
//            }
            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }

                // Sends the received line to the server.
                //create a document of type message

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
        } finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }
    }
}

//TODO:
//1- identify for each server instance a unique port(?) and a unique server ID (create server ID for each instance of SecureChatServer)
//2- Each Instance should act as a group chat room? (should think about that. Because we dont want to create 2 different servers for the same group chat)
//3- Each message sent by a client and received by a server, is stored inside the "messages" collection on Firebase
//4-


//List of different collections: 1- Server   2- Messages {serverid_userid_messaage_timestamp}
// To start creating a group chat , The user creates a securechatserver instance with a unique ID. IT's created in the database ith a unique port as well
// The user sends the id to other contacts/people
// 