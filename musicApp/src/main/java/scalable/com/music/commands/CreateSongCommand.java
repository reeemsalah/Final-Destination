package scalable.com.music.commands;

import com.arangodb.entity.BaseEdgeDocument;
import org.json.JSONArray;
import org.json.JSONML;
<<<<<<< HEAD
import org.json.JSONObject;
=======
import org.json.simple.JSONObject;
>>>>>>> dd1bd749094cfbbba3e14dfd64f08993a2a9c1d0
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.CommandVerifier;
import com.arangodb.entity.BaseDocument;
import scalable.com.shared.classes.MinIo;
import scalable.com.shared.classes.Responder;
import javax.validation.constraints.NotBlank;
<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> dd1bd749094cfbbba3e14dfd64f08993a2a9c1d0

public class CreateSongCommand  extends MusicCommand {
    //@NotBlank(message = "name should not be empty")
    private String name;
    //@NotBlank(message = "artists should not be empty")
    private String[] artists;
    //@NotBlank(message = "genres should not be empty")
    private String[] genres;
    //@NotBlank(message = "album_id should not be empty")
    private String album_id;

    @Override
    public String getCommandName() {
        return "CreateSong";
    }

    @Override
    public String execute() {

        Arango arango = Arango.getInstance();
<<<<<<< HEAD
        JSONObject response=new JSONObject();
=======
>>>>>>> dd1bd749094cfbbba3e14dfd64f08993a2a9c1d0

        try {
            if (this.tokenPayload==null)
                return Responder.makeErrorResponse("No token provided", 404);
            if (this.tokenPayload.get("isArtist").equals("false"))
                return Responder.makeErrorResponse("Only artists can create songs", 404);

            arango.createDatabaseIfNotExists("Spotify");
            arango.createCollectionIfNotExists("Spotify","Songs",false);
            //extract name
            this.name = body.getString("name");
            //extract album id
            this.album_id = body.getString("album_id");
            //extract artists
            this.artists = body.getString("artists").split(",");
            //extract album id
            this.genres = body.getString("genres").split(",");

            //name for song/cover files
            String coverAndAudioName=body.getString("name")+body.getString("artists");

            //UPLOADING SONG
            String songUrl;
            try {
                songUrl = MinIo.uploadObject("music-app-songbucket", coverAndAudioName, files.getJSONObject("song"));
                if (songUrl.isEmpty())
                    return Responder.makeErrorResponse("Error Occurred While Uploading Your Song!", 404);
            } catch (Exception e) {
                return Responder.makeErrorResponse(e.getMessage(), 400);
            }
            //UPLOADING COVER
            String coverUrl;
            try {
                coverUrl = MinIo.uploadObject("music-app-coverbucket", coverAndAudioName, files.getJSONObject("cover"));
                if (coverUrl.isEmpty())
                    return Responder.makeErrorResponse("Error Occurred While Uploading Your Cover Image!", 404);
            } catch (Exception e) {
                return Responder.makeErrorResponse(e.getMessage(), 400);
            }

            BaseDocument myDocument = new BaseDocument();
            myDocument.addAttribute("name", this.name);
            myDocument.addAttribute("artists", this.artists);
            myDocument.addAttribute("genres", this.genres);
            myDocument.addAttribute("album_id", this.album_id);
            myDocument.addAttribute("audio", songUrl);
            myDocument.addAttribute("cover", coverUrl);

            //Default attributes
            myDocument.addAttribute("rating", 0);
            myDocument.addAttribute("number_times_rated", 0);
<<<<<<< HEAD
            myDocument.addAttribute("people_rated",new ArrayList<Integer>());
=======
            myDocument.addAttribute("people_rated",new int[0]);
>>>>>>> dd1bd749094cfbbba3e14dfd64f08993a2a9c1d0
            myDocument.addAttribute("number_of_streams", 0);
            myDocument.addAttribute("likes", 0);

            BaseDocument res = arango.createDocument("Spotify", "Songs", myDocument);

<<<<<<< HEAD
            //CREATE JSON RESPONSE
            res.getProperties().forEach((key, value) ->
                    response.put(key, value)
            );
            response.put("id",res.getKey());
=======
>>>>>>> dd1bd749094cfbbba3e14dfd64f08993a2a9c1d0

        } catch (Exception e) {
            return Responder.makeErrorResponse(e.getMessage(), 404);
        }
<<<<<<< HEAD
        return Responder.makeDataResponse(response);
=======
        return Responder.makeMsgResponse("Song created!");
>>>>>>> dd1bd749094cfbbba3e14dfd64f08993a2a9c1d0
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
            //extract name
            this.name = body.getString("name");
            //extract album id
            this.album_id = body.getString("album_id");
            //extract artists
            this.artists = body.getString("artists").split(",");
            //extract album id
            this.genres = body.getString("genres").split(",");

        }
        catch (Exception e){
            System.out.println(body);
            throw new ValidationException("attributes data types are wrong: "+e.getMessage());
        }
    }
}