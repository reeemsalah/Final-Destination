package scalable.com.music.commands;
import com.arangodb.entity.BaseDocument;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.Responder;
import java.util.ArrayList;

public class RateMusicTrackCommand extends MusicCommand {

    @Override
    public String getCommandName() {
        return "RateMusicTrack";
    }
    @Override
    public String execute(){

        Arango arango;
        try{
            String songIdentifier = body.getString("song_id");
            int userId = Integer.parseInt(this.tokenPayload.getString("id"));
            int userRating = body.getInt("rating");

            arango = Arango.getInstance();

            BaseDocument toRead = arango.readDocument("spotifyArangoDb",
                    "Songs", songIdentifier);
            Double oldRating = (Double)(toRead.getAttribute("Rating"));
            int totalRatings = (Integer)(toRead.getAttribute("number_times_rated"));
            ArrayList<Integer> peopleRated = (ArrayList<Integer>)
                    (toRead.getAttribute("people_rated"));
            if(peopleRated.contains(userId)){
                return Responder.makeMsgResponse("You have already rated the song!");
            }
            else {

                Double newRating = (oldRating * totalRatings + userRating) / (totalRatings + 1);
                int newtotalRatings = totalRatings + 1;
                peopleRated.add(userId);


                toRead.updateAttribute("Rating", newRating);
                toRead.updateAttribute("number_times_rated", newtotalRatings);
                toRead.updateAttribute("people_rated", peopleRated);

                arango.updateDocument("spotifyArangoDb", "Songs", toRead, songIdentifier);

                return Responder.makeMsgResponse("successfully rated the playlist");
            }
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


    }
    }
