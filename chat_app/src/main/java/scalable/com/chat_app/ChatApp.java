package scalable.com.chat_app;

import scalable.com.chat_app.commands.Quickstart;
import scalable.com.shared.App;
import scalable.com.shared.classes.Arango;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChatApp extends App{
    public static Quickstart qs;

    public static void main(String[] args) throws TimeoutException, IOException, ClassNotFoundException {
        System.out.println("ttab here");
        ChatApp app = new ChatApp();

        app.dbInit();
        app.start();
    }

    @Override
    public void dbInit() throws IOException {
        System.out.println("ttab here 2");
        Quickstart qs = new Quickstart();
        qs = qs;
        System.out.println("e7na hena");
    }

    @Override
    protected String getAppName() {
        return "ChatApp";
    }
}