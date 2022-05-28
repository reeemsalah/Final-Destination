package com.scalable.recommendations.commands;

import com.arangodb.ArangoCursor;
import com.arangodb.entity.BaseDocument;
import com.arangodb.velocypack.VPackSlice;
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
        //TODO get user_id from token
        //int user_id =Integer.parseInt(this.tokenPayload.getString("id"));
        int user_id = 1;
        int limitno = 1;
        Arango arango = Arango.getInstance();
        String arango_default_id = (String) get_arango_default_id(user_id, arango);
        System.out.println(arango_default_id);
        if(arango_default_id != null){
            String aqlQuery = String.format("LET similar_users = (FOR songs_history IN 1..1 OUTBOUND @user_id @@collection_name FOR user IN 1..1 INBOUND songs_history._id @@collection_name FILTER user._id != @user_id RETURN user) LET most_similar_users =(FOR similar_user IN similar_users COLLECT id = similar_user._id, name = similar_user.id, isArtist = similar_user.isArtist WITH COUNT INTO similarityCount SORT similarityCount DESC LIMIT @limitno RETURN {id,name,isArtist}  ) LET curr_user_history = (FOR songs_history IN 1..1 OUTBOUND @user_id @@collection_name RETURN TO_ARRAY(songs_history._id)) LET recommended = (FOR most_similar_user IN most_similar_users FOR song IN 1..1 OUTBOUND most_similar_user.id @@collection_name RETURN DISTINCT MINUS(FLATTEN(TO_ARRAY(song._id)),FLATTEN(curr_user_history))) FOR r in recommended FILTER length(r)>0 RETURN r");
            HashMap<String, Object> bindVars = new HashMap<>();
            bindVars.put("@collection_name",DatabaseConstants.MUSIC_EDGE_COLLECTION);
            bindVars.put("limitno",limitno);
            bindVars.put("user_id",arango_default_id);
            ArangoCursor<List> cursor = arango.query2( DatabaseConstants.DATABASE_NAME, aqlQuery, bindVars);
            for (List element : cursor) {
                System.out.println(element.toString());
            }
        }

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
                System.out.println("cursor not null");
                List<BaseDocument> user = cursor.asListRemaining();
                if (user.size() == 1) {
                    for (BaseDocument result_row : user) {
                        //result_row ==> [documentRevision=_eOaJKBW---, documentHandle=users/159547, documentKey=159547, properties={id=1, isArtist=0}]
                        return result_row.getId();
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
        //TODO GET TOKEN
        return false;
    }

    @Override
    public void validateAttributeTypes() throws ValidationException {

    }
}
