package scalable.com.music.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.CommandVerifier;
import com.arangodb.entity.BaseDocument;


public class CreatePlaylistCommand extends CommandVerifier {
    @Override
    public String getCommandName() {
        return "CreatePlaylist";
    }

    @Override
    public String execute() {
        Arango arango = Arango.getInstance();
        arango.createCollectionIfNotExists("Spotify","Playlists",true);
        //BaseDocument document = new BaseDocument();
        return null;
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

    }
}
