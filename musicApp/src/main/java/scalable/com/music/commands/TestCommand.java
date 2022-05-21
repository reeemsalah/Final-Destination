package scalable.com.music.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Responder;

public class TestCommand extends MusicCommand{
    @Override
    public String getCommandName() {
        return "Test";
    }

    @Override
    public String execute() {

             System.out.println("executing test commands");

        return Responder.makeMsgResponse("ya walaaaaaaaaaa");
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
