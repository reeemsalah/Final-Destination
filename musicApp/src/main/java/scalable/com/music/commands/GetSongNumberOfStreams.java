package scalable.com.music.commands;

import com.arangodb.entity.BaseEdgeDocument;
import org.json.JSONArray;
import org.json.JSONML;
import org.json.simple.JSONObject;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.CommandVerifier;
import com.arangodb.entity.BaseDocument;
import scalable.com.shared.classes.MinIo;
import scalable.com.shared.classes.Responder;
import javax.validation.constraints.NotBlank;

public class GetSongNumberOfStreams  extends MusicCommand {
//    @NotBlank(message = "song_id should not be empty")
    private String song_id;

    @Override
    public String getCommandName() {
        return "GetSongNumberOfStreams";
    }

    @Override
    public String execute() {
        BaseDocument res;
        Arango arango = Arango.getInstance();

        try {

            arango.createDatabaseIfNotExists("Spotify");
            arango.createCollectionIfNotExists("Spotify","Songs",false);
            //extract song id
            this.song_id = body.getString("song_id");
            res = arango.readDocument("Spotify","Songs",song_id);

        } catch (Exception e) {
            return Responder.makeErrorResponse(e.getMessage(), 404);
        }
        return Responder.makeMsgResponse(res.getAttribute("number_of_streams")+ "");
    }

    @Override
    public String getRestAPIMethod() {
        return "POST";
    }

    @Override
    public boolean isAuthNeeded() {
        return false;
    }

    @Override
    public void validateAttributeTypes() throws ValidationException {

        try {
            //extract song id
            this.song_id = body.getString("song_id");

        }
        catch (Exception e){
            System.out.println(body);
            throw new ValidationException("attributes data types are wrong: "+e.getMessage());
        }
    }
}