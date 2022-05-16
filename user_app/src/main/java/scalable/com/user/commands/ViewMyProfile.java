package scalable.com.user.commands;

import scalable.com.exceptions.ValidationException;

public class ViewMyProfile extends UserCommand {
    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public String execute() {
        return null;
    }

    @Override
    public String getRestAPIMethod() {
        return null;
    }

    @Override
    public boolean isAuthNeeded() {
        return false;
    }

    @Override
    public void validateAttributeTypes() throws ValidationException {

    }
}
