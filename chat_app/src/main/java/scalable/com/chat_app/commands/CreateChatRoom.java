package scalable.com.chat_app.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Responder;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

public class CreateChatRoom extends ChatAppCommand {



    @NotBlank(message="room name shouldnt be empty")
    private String roomName;



    @Override
    public String getCommandName() {
        return "CreateChatRoom";
    }

    @Override
    public String execute() {

        try {
            System.out.println("ana hena tab");
            int id=Integer.parseInt(this.tokenPayload.getString("id"));
            Quickstart qs = null;
            qs = new Quickstart();
            System.out.println("made it here");
            HashMap<String, Object> data = new HashMap<>();
            Date date = new Date();
            Timestamp timestamp2 = new Timestamp(date.getTime());
            data.put("creationDate",timestamp2.toString() );
            data.put("roomName", this.roomName);
            data.put("user" , id);
            try {
                qs.addDocument("Rooms" , id+roomName,data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "we are here";
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
