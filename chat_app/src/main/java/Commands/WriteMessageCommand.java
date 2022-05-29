package Commands;

public class WriteMessageCommand extends Command{


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
