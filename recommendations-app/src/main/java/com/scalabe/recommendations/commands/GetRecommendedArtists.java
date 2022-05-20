package com.scalabe.recommendations.commands;

import scalable.com.exceptions.ValidationException;

public class GetRecommendedArtists extends  RecommendationsCommand
{
    @Override
    public String getCommandName() {
        return "GetRecommendedArtists";
    }

    @Override
    public String execute() {
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
