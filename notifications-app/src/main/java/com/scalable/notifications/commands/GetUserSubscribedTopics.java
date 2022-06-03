package com.scalable.notifications.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.CommandVerifier;

public class GetUserSubscribedTopics extends CommandVerifier {
    @Override
    public String getCommandName() {
        return "GetUserSubscribedTopics.properties";
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
