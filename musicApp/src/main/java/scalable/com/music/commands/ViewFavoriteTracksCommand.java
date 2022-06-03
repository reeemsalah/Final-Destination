package scalable.com.music.commands;

import com.arangodb.entity.BaseDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.CommandVerifier;
import scalable.com.shared.classes.Responder;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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
            JSONObject al = new JSONObject();
            ArrayList<BaseDocument> docs = new ArrayList<>();
            Object[] albums = a.stream().toArray();
            JSONArray toCombine = new JSONArray();
            for(int i = 0; i < albums.length; i++){
                BaseDocument th = (BaseDocument) albums[i];
                Map fordoc = th.getProperties();
                JSONObject f = new JSONObject(fordoc);
                toCombine.put(f);
            }
            JSONObject combined = new JSONObject();
            for(int i = 0; i < toCombine.length(); i++){
                JSONObject current = toCombine.getJSONObject(i);
                Set<String> keys = current.keySet();
                for(String s : keys){
                    if(!s.equals("user_id"))
                        combined.append(s, current.get(s));
                }
            }

            return Responder.makeDataResponse(combined);
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
