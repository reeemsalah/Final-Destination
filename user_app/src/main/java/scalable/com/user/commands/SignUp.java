package scalable.com.user.commands;

import scalable.com.exceptions.ValidationException;

public class SignUp extends UserCommand  {

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
        return "POST";
    }

    @Override
    public boolean isAuthNeeded() {
        return false;
    }

    @Override
    public void validateAttributeTypes() throws ValidationException {

    }
}
