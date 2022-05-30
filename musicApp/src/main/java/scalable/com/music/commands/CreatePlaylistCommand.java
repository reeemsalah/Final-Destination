package scalable.com.music.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.CommandVerifier;
import com.arangodb.entity.BaseDocument;
import java.util.UUID;

import scalable.com.shared.classes.Responder;


public class CreatePlaylistCommand extends CommandVerifier {
    @Override
    public String getCommandName() {
        return "CreatePlaylist";
    }

    @Override
    public String execute() {
        Arango arango = null;
         try {
             UUID id = UUID.randomUUID();
            String name = body.getString("name");
            Boolean isVisible = true;
            Double rating = 0.0;
        
            // TODO:String UserId = authenticationParams.getString(ThreadCommand.USERNAME);
            // TODO: auto generated id             uu id

            arango = Arango.getInstance();
            arango.createCollectionIfNotExists("Spotify","Playlists",false);
            BaseDocument myDocument = new BaseDocument();
            //TODO : key of document
          //  myDocument.setKey(id.toString());
           myDocument.addAttribute("name",name);
            myDocument.addAttribute("isVisible", isVisible);
            myDocument.addAttribute("Rating", rating);
           BaseDocument res = arango.createDocument("Spotify","Playlists", myDocument);
            return Responder.makeMsgResponse("Created Playlist");
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
        return false;
    }

    @Override
    public void validateAttributeTypes() throws ValidationException {

    }
}
