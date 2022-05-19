package main.java.scalable.com.recommendations.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Responder;

import javax.validation.constraints.NotBlank;

public class GetPopularArtistsCommand extends RecommendationsCommand{
    
    @Override
    public String getCommandName() {
        return "GetPopularArtists";
    }

    @Override
    public String execute() {
        return Responder.makeMsgResponse("Popular Artists");
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
