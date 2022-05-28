package scalable.com.music.commands;

import com.arangodb.entity.BaseDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.CommandVerifier;
import scalable.com.shared.classes.Responder;

import java.lang.reflect.Array;

public class ViewFavoriteAlbumsCommand extends CommandVerifier {
    @Override
    public String getCommandName() {
        return "ViewFavoriteAlbums";
    }

    @Override
    public String execute() {
        Arango arango = null;
        try {
            String user_id = this.tokenPayload.getString("id");
            //String w = this.uriParams.getString("user_id");
                //changesssssss
            arango = Arango.getInstance();
            arango.createCollectionIfNotExists("spotifyArangoDb","FavoriteAlbums",false);
            var a = arango.filterCollection("spotifyArangoDb", "FavoriteAlbums", "user_id", user_id);
            JSONObject al = new JSONObject();
            //Object[] albums = a.stream().toArray();
//            for(int i = 0; i < albums.length; i++){
//
//            }

            return Responder.makeMsgResponse("got so far");
        } catch (Exception e) {
            return Responder.makeErrorResponse(e.getMessage(), 404);
        }
    }

    @Override
    public String getRestAPIMethod() {
        return "GET";
    }

    @Override
    public boolean isAuthNeeded() {
        return true;
    }

    @Override
    public void validateAttributeTypes() throws ValidationException {

    }
}
