package Commands;

import java.util.concurrent.ExecutionException;

public class ReadChatCommand extends Command{

    public void execute() throws ExecutionException, InterruptedException {
        super.execute();

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
