package scalable.com.user_to_user.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Responder;

import javax.validation.constraints.NotBlank;

public class GetFollowersCommand extends UserToUserCommand{

    @Override
    public String getCommandName() {
        return "GetFollowers";
    }

    @Override
    public String execute() {
        return Responder.makeMsgResponse("followers for now");
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
