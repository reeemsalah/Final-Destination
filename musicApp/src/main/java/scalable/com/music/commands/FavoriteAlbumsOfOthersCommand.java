package scalable.com.music.commands;

import org.json.JSONObject;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.CommandVerifier;
import scalable.com.shared.classes.Responder;

public class FavoriteAlbumsOfOthersCommand extends CommandVerifier {
    @Override
    public String getCommandName() {
        return "FavoriteAlbumsOfOthers";
    }

    @Override
    public String execute() {
        Arango arango = null;
        try {
            String user_id = this.uriParams.getString("user_id");

            arango = Arango.getInstance();
            arango.createCollectionIfNotExists("spotifyArangoDB","FavoriteAlbums",false);
            var a = arango.filterCollection("spotifyArangoDB", "FavoriteAlbums", "user_id", user_id);
            JSONObject al = new JSONObject();
            Object[] albums = a.stream().toArray();
            for(int i = 0; i < albums.length; i++){

            }

            return Responder.makeMsgResponse(albums.toString());
        } catch (Exception e) {
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
