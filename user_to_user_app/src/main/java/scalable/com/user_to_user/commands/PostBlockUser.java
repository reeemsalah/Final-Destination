package scalable.com.user_to_user.commands;

import javax.validation.constraints.NotNull;

import com.arangodb.entity.BaseDocument;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.Responder;


import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class PostBlockUser extends UserToUserCommand{

    @NotNull(message="blocked user username should not be empty")
    private int blocked_id;


    @Override
    public String execute() {

        int id=Integer.parseInt(this.tokenPayload.getString("id"));
        Arango arango = null;
        try {
            arango = Arango.getInstance();
            arango.createCollectionIfNotExists("user_to_user","blocked_ids",false);
            BaseDocument dbBlock = new BaseDocument();
            dbBlock.addAttribute("user_id",id);
            dbBlock.addAttribute("blocked_id", this.blocked_id);
            BaseDocument res = arango.createDocument("user_to_user","blocked_ids", dbBlock);
            System.out.println("Block "+res+" added");
            return Responder.makeMsgResponse("Blocked: "+blocked_id);
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
            this.blocked_id = body.getInt("blocked_id");
        }
        catch (Exception e){
            throw new ValidationException("attributes data types are wrong");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(this);
        if(!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(cv -> cv.getMessage())
                    .collect(Collectors.joining(", "));
            throw new ValidationException(errorMessage);
        }
    }


    @Override
    public String getCommandName() {
        return "PostBlockUser";
    }

}
