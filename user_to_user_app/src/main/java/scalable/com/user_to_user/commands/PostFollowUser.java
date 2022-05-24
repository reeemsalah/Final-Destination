package scalable.com.user_to_user.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.CommandVerifier;
import scalable.com.shared.classes.Responder;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;

public class PostFollowUser extends UserToUserCommand{

    @NotBlank(message="username should not be empty")
    private String username;
    @NotBlank(message="blocked user username should not be empty")
    private String user;


    @Override
    public String execute() {
        return Responder.makeMsgResponse(" ha implement el follow mn 3enaya ");
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

            this.username = body.getString("username");
            this.user = body.getString("user");
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
        return "PostFollowUser";
    }
}
