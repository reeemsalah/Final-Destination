package Commands;

import com.google.firebase.database.annotations.NotNull;
import scalable.com.exceptions.ValidationException;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class CreateChatRoomCommand extends ChatAppCommand{



    @NotBlank(message="room name shouldnt be empty")
    private String roomName;



    @Override
    public String getCommandName() {

        return "CreateChatRoom";
    }

    @Override
    public String execute() throws Exception {
        int id=Integer.parseInt(this.tokenPayload.getString("id"));

        Quickstart qs = new Quickstart();
        HashMap<String, Object> data = new HashMap<>();
        Date date = new Date();
        Timestamp timestamp2 = new Timestamp(date.getTime());
        data.put("creationDate",timestamp2.toString() );
        data.put("roomName", this.roomName);
        data.put("user" , id);
        qs.addDocument("Rooms" , id+roomName,data);

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
        try {
            this.roomName = body.getString("roomName");
        }
        catch (Exception e){
            throw new ValidationException("attributes data types are wrong");
        }
        this.validateAnnotations();
    }
}
