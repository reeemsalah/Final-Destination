package scalable.com.music.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.CommandVerifier;
import scalable.com.shared.classes.Responder;

public class ViewFavoriteTracksCommand extends CommandVerifier {
    @Override
    public String getCommandName() {
        return "ViewFavoriteTracks";
    }

    @Override
    public String execute() {
        Arango arango = null;
        try {
            String user_id = this.tokenPayload.getString("id");

            arango = Arango.getInstance();
            arango.createCollectionIfNotExists("spotifyArangoDb","FavoriteTracks",false);
            var a = arango.filterCollection("spotifyArangoDb", "FavoriteTracks", "user_id", user_id);
            Object[] tracks = a.stream().toArray();
            return Responder.makeMsgResponse(tracks.toString());
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
