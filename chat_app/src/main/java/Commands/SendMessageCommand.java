package Commands;

import scalable.com.exceptions.ValidationException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class SendMessageCommand extends ChatAppCommand{
    String message;
    String roomID;


    @Override
    public String getCommandName() {
        return "SendMessage";
    }

    @Override
    public String execute() throws Exception {
        int id=Integer.parseInt(this.tokenPayload.getString("id"));
        Quickstart qs = new Quickstart();

        Date date = new Date();
        HashMap<String, Object> data = new HashMap<>();
        Timestamp timestamp2 = new Timestamp(date.getTime());
        data.put("message", this.message);
        data.put("date",timestamp2.toString());
        data.put("room", this.roomID);
        data.put("user" , id);
        String uniqueID = UUID.randomUUID().toString();
        qs.addDocument("MessagesNew" , uniqueID+ timestamp2.toString(),data);
        return null;
    }

    @Override
    public String getRestAPIMethod() {
        return "POST";
    }

    @Override
    public boolean isAuthNeeded() {
        return true;
    }

    @Override
    public void validateAttributeTypes() throws ValidationException {

    }
}
