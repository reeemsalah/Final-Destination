package scalable.com.user_to_user.commands;

import javax.validation.constraints.NotNull;

import com.arangodb.entity.BaseDocument;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.Responder;


public class PostFollowUser extends UserToUserCommand{

    @NotNull(message="followed user id should not be empty")
    private int followed_id;


    @Override
    public String execute() {

        int id=Integer.parseInt(this.tokenPayload.getString("id"));
        Arango arango = null;
        try {
            arango = Arango.getInstance();
            arango.createCollectionIfNotExists("user_to_user","followed_ids",false);
            BaseDocument dbFollow = new BaseDocument();
            dbFollow.addAttribute("user_id",id);
            dbFollow.addAttribute("followed_id", this.followed_id);
            BaseDocument res = arango.createDocument("user_to_user","followed_ids", dbFollow);
            System.out.println("Follow "+res+" added");
            return Responder.makeMsgResponse("Following: "+followed_id);
        } catch (Exception e) {
            return Responder.makeErrorResponse(e.getMessage(), 404);
        }
    }


    @Override
    public String getRestAPIMethod() {
        return "POST";
    }


    @Override
    public boolean isAuthNeeded() {
        return true;
    }


    @Override
    public void validateAttributeTypes() throws ValidationException {
        try {

            this.followed_id = body.getInt("followed_id");
        }
        catch (Exception e){
            throw new ValidationException("attributes data types are wrong");
        }
        this.validateAnnotations();

    }


    @Override
    public String getCommandName() {
        return "PostFollowUser";
    }
}
