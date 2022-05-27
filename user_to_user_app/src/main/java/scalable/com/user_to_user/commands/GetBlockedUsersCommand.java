package scalable.com.user_to_user.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Responder;

import javax.validation.constraints.NotBlank;

public class GetBlockedUsersCommand extends UserToUserCommand{

    @Override
    public String getCommandName() {
        return "GetBlockedUsers";
    }

    @Override
    public String execute() {
        return Responder.makeMsgResponse("blocked users for now");
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
