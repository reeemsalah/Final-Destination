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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class RateMusicTrackCommand extends MusicCommand {
    @NotBlank(message = "please give a rating!")
    private int rating;
    private String song_id;
    @Override
    public String getCommandName() {
        return "RateMusicTrack";
    }
    @Override
    public String execute(){

        Arango arango = null;
        try{
            String songIdentifier = body.getString(song_id);
            int userId = Integer.parseInt(this.tokenPayload.getString("id"));
            int userRating = body.getInt("rating");

            arango = Arango.getInstance();

            BaseDocument toRead = arango.readDocument("Spotify",
                    "Songs", songIdentifier);
            Double oldRating = (Double)(toRead.getAttribute("Rating"));
            int totalRatings = (Integer)(toRead.getAttribute("number_times_rated"));
            ArrayList<Integer> peopleRated = (ArrayList<Integer>)
                    (toRead.getAttribute("people_rated"));

            Double newRating = (oldRating*totalRatings+userRating)/(totalRatings+1);
            int newtotalRatings = totalRatings +1;
            peopleRated.add(userId);


                toRead.updateAttribute("Rating", newRating);
                toRead.updateAttribute("number_times_rated", newtotalRatings);
                toRead.updateAttribute("people_rated", peopleRated);

                arango.updateDocument("Spotify", "Songs", toRead, songIdentifier);

                return Responder.makeMsgResponse("successfully rated the playlist");
            }
            catch (Exception e) {
                return Responder.makeErrorResponse(e.getMessage(), 404);
            }


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
