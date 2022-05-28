package scalable.com.music.commands;
import com.arangodb.entity.BaseDocument;
import org.json.JSONObject;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.App;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.PostgresConnection;
import scalable.com.shared.classes.Responder;

import javax.validation.constraints.NotBlank;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
public class RateMusicTrackCommand extends MusicCommand {
    @NotBlank(message = "please give a rating!")
    private int rating;
    @Override
    public String getCommandName() {
        return "RateMusicTrack";
    }
    @Override
    public String execute(){


        return Responder.makeMsgResponse("successfully rated");
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
        try{
            this.rating = body.getInt("rating");
        }
        catch (Exception e){
            throw new ValidationException("attributes data types are wrong");
        }
        this.validateAnnotations();

    }
    }
