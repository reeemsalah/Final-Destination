package com.scalabe.recommendations.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Responder;

public class GetRecommendedMusicTracks extends RecommendationsCommand{

    @Override
    public String getCommandName() {
        return "GetRecommendedMusicTracks";
    }

    @Override
    public String execute() {
        return Responder.makeMsgResponse("Recommended Music Tracks");
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
