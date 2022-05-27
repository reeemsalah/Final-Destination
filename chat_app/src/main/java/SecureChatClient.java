import com.google.cloud.firestore.QueryDocumentSnapshot;
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
import java.security.KeyStore;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


public final class SecureChatClient {

    static final String HOST = System.getProperty("host", "0.0.0.0");
    int PORT = Integer.parseInt(System.getProperty("port", "4040"));
    public  String clientUsername;
    public ArrayList<MessageRecord> messages = new ArrayList<>();
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

        System.out.println("here is the past chat history in this server : ");
        ArrayList<QueryDocumentSnapshot> arr = qs.retrieveAllDocuments("Messages");
        ArrayList<HashMap<String,Object>> records = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            QueryDocumentSnapshot document = arr.get(i);
             System.out.println(document.get("server"));
             if((document.get("server")+"").equals(PORT+""))
             {
                records.add((HashMap<String, Object>) document.getData());
             }
        }


        for (int i = 0; i <records.size() ; i++) {

            String message = (String) records.get(i).get("message");
            String date = (String) records.get(i).get("date");
            String user = (String) records.get(i).get("user");

            MessageRecord messagerec = new MessageRecord(message , date , user);
            this.messages.add(messagerec);

        }
        Collections.sort(messages, (o1, o2) -> o1.date.compareTo(o2.date));
        for (int i = 0; i < messages.size(); i++) {
           System.out.println("[" + messages.get(i).date + "] " + messages.get(i).user + " : " + messages.get(i).message);
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
                Date date = new Date();
                HashMap<String, Object> data = new HashMap<>();
                Timestamp timestamp2 = new Timestamp(date.getTime());
                data.put("message", line);
                data.put("date",timestamp2.toString() );
                data.put("server", this.PORT);
                data.put("user" , this.clientUsername);
                qs.addDocument("Messages" , this.clientUsername+this.PORT+ timestamp2.toString(),data);
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

    private static <K extends Comparable<K>, V extends Comparable<V>> Map<K, V> sort(
            final Map<K, V> unsorted,
            final boolean order) {
        final var list = new LinkedList<>(unsorted.entrySet());

        list.sort((o1, o2) -> order
                ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue())
                : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new
                )
        );
    }

    class MessageRecord{
        String message;
        String date;
        String user;

        public MessageRecord(String message, String date, String user){
            this.message = message;
            this.date = date;
            this.user = user;

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