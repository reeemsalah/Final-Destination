package com.scalable.recommendations.commands;

import com.arangodb.entity.BaseDocument;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.Responder;

import javax.validation.constraints.NotBlank;

public class CreateUserEdge extends RecommendationsCommand {
    @NotBlank(message = "Must Specify whether followed user id")
    private int followed_user_id;
    @Override
    public String getCommandName() {
        return "CreateUserEdge";
    }

    @Override
    public String execute() {
         int user_id =Integer.parseInt(this.tokenPayload.getString("id"));
        Arango arango;
        try{
            arango=Arango.getInstance();
            arango.getSingleEdgeId("spotifyArangoDB","users",user_id+"",followed_user_id+"");


        } catch (Exception e){
            Responder.makeErrorResponse(e.getMessage(),404);
        }
        return Responder.makeMsgResponse("User Edge Created Successfully");
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
