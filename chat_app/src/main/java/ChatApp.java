
import Commands.Quickstart;
import scalable.com.shared.App;
import scalable.com.shared.classes.Arango;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChatApp extends App{
    public static Quickstart qs;

    public static void main(String[] args) throws TimeoutException, IOException, ClassNotFoundException {

        ChatApp app = new ChatApp();

        app.dbInit();
        app.start();
    }

    @Override
    public void dbInit() throws IOException {
        Quickstart qs = new Quickstart();
        qs = qs;
    }

    @Override
    protected String getAppName() {
        return "ChatApp";
    }
}
