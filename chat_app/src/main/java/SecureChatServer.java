import com.google.firebase.cloud.FirestoreClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.example.telnet.TelnetServer;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.*;


import java.io.IOException;
import java.security.cert.CertificateException;


public final class SecureChatServer {


     int PORT = 0;
     //should the port change? should it be static for all servers? idk. we will find out.
    Quickstart qs;
    int serverID;



    public SecureChatServer(int serverID , int port ) throws IOException {
        Quickstart qs = new Quickstart();
        this.qs = qs;
        this.serverID = serverID;
        this.PORT =    Integer.parseInt(System.getProperty("port", port+""));

        

        
    }

    public static void main(String[] args) throws Exception {
        int port = 4040;
        SecureChatServer server = new SecureChatServer(0,port)   ;
        server.execute();

    }

    public void execute() throws IOException, CertificateException, InterruptedException {
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())
                .build();

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new SecureChatServerInitializer(sslCtx));

            b.bind(this.PORT).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
