package com.scalable.recommendations.commands;

import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.BaseEdgeDocument;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.Responder;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class CreateUserNode extends  RecommendationsCommand {
//    @NotBlank(message = "Must Specify whether the user is artist ot not")
    private Boolean is_artist;
    private String user_id;
    @Override
    public String getCommandName() {
        return "CreateUserNode";
    }

    @Override
    public String execute() {


        Arango arango;
        try{

            arango=Arango.getInstance();
            System.out.println("Got instance");

            System.out.println(arango.containsCollection("spotifyArangoDb","users"));

            BaseDocument userNode = new BaseDocument();
            userNode.setKey(user_id);
            userNode.addAttribute("is_artist",is_artist);

             arango.createDocument("spotifyArangoDb","users",userNode) ;
        } catch (Exception e){
            Responder.makeErrorResponse(e.getMessage(),404);
        }
        return Responder.makeMsgResponse("Node User Created Successfully");

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
        try{
            this.is_artist=body.getBoolean("is_artist");
            this.user_id=body.getString("user_id");

        }   catch (Exception e){
            e.printStackTrace();
            throw new ValidationException("attributes data types are wrong: "+e.getMessage());
        }

    }
}
