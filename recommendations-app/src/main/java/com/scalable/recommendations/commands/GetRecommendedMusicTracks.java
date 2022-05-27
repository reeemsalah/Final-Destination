package com.scalable.recommendations.commands;

import com.arangodb.ArangoCursor;
import com.arangodb.entity.BaseDocument;
import com.scalable.recommendations.constants.DatabaseConstants;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.Responder;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GetRecommendedMusicTracks extends RecommendationsCommand{
    //input nothing, ana ma3aya el userID
    //output recommended music tracks
    @Override
    public String getCommandName() {
        return "GetRecommendedMusicTracks";
    }

    @Override
    public String execute() {
        System.out.println("I am executing the GetRecommendedMusicTracks command");
        int user_id =Integer.parseInt(this.tokenPayload.getString("id"));
        Arango arango = Arango.getInstance();
        Object arango_default_id =  get_arango_default_id(user_id, arango);
        System.out.println(arango_default_id.toString());
        return Responder.makeMsgResponse("Recommended Music Tracks");
    }

    public Object get_arango_default_id(int user_id,Arango arango){

        try {
            HashMap<String, Object> bindVars = new HashMap<>();
            String aqlQuery = String.format("FOR user IN @@collection FILTER user.id == @userid RETURN user");
            bindVars.put("@collection", DatabaseConstants.USER_DOCUMENT_COLLECTION);
            bindVars.put("userid", user_id);
            ArangoCursor<BaseDocument> cursor = arango.query(DatabaseConstants.DATABASE_NAME, aqlQuery, bindVars);
            if (!(Objects.isNull(cursor))) {
                List<BaseDocument> user = cursor.asListRemaining();
                if (user.size() == 1) {
                    for (BaseDocument result_row : user) {
                        return result_row.getAttribute("_id");
                    }
                }
            } else {
                Responder.makeMsgResponse("No user exists with that id");
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
            return  Responder.makeErrorResponse("Something went wrong",400);
        }
        return null;
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
