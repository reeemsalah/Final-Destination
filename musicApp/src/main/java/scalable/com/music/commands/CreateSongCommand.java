package scalable.com.music.commands;

import com.arangodb.entity.BaseEdgeDocument;
import org.json.JSONArray;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.CommandVerifier;
import com.arangodb.entity.BaseDocument;
import scalable.com.shared.classes.Responder;

import javax.validation.constraints.NotBlank;

public class CreateSongCommand  extends CommandVerifier {
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

        try {

            arango.createDatabaseIfNotExists("Spotify");
            arango.createCollectionIfNotExists("Spotify","Songs",false);
            //extract name
            this.name = body.getString("name");
            //extract artists array
            JSONArray artistsJson = body.getJSONArray("artists");
            this.artists = new String[artistsJson.length()];
            for(int i = 0; i < artistsJson.length(); i++)
                this.artists[i] = artistsJson.getString(i);
            //extract genres array
            JSONArray genresJson = body.getJSONArray("genres");
            this.genres = new String[genresJson.length()];
            for(int i = 0; i < genresJson.length(); i++)
                this.genres[i] = genresJson.getString(i);
            //extract album id
            this.album_id = body.getString("album_id");
            
            BaseDocument myDocument = new BaseDocument();
            myDocument.addAttribute("name", this.name);
            myDocument.addAttribute("artists", this.artists);
            myDocument.addAttribute("genres", this.genres);
            myDocument.addAttribute("album_id", this.album_id);

            //Default attributes

            myDocument.addAttribute("rating", 0.0);
            myDocument.addAttribute("number_of_streams", 0);
            myDocument.addAttribute("likes", 0);
            myDocument.addAttribute("audio", "");

            BaseDocument res = arango.createDocument("Spotify", "Songs", myDocument);


        } catch (Exception e) {
            return Responder.makeErrorResponse(e.getMessage(), 404);
        }
        return Responder.makeMsgResponse("SUCCESS!");
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
            //extract name
            this.name = body.getString("name");
            //extract artists array
            JSONArray artistsJson = body.getJSONArray("artists");
            this.artists = new String[artistsJson.length()];
            for(int i = 0; i < artistsJson.length(); i++)
                this.artists[i] = artistsJson.getString(i);
            //extract genres array
            JSONArray genresJson = body.getJSONArray("genres");
            this.genres = new String[genresJson.length()];
            for(int i = 0; i < genresJson.length(); i++)
                this.genres[i] = genresJson.getString(i);
            //extract album id
            this.album_id = body.getString("album_id");

        }
        catch (Exception e){
            throw new ValidationException("attributes data types are wrong: "+e.getMessage());
        }
    }
}