package scalable.com.user_to_user.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Responder;
import scalable.com.shared.classes.CommandVerifier;

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

public class GetTotalListenersCommand extends UserToUserCommand{

    @NotBlank(message="Artist name should not be empty")
    private String artist;

    @Override
    public String getCommandName() {
        return "GetTotalListeners";
    }

    @Override
    public String execute() {
        return Responder.makeMsgResponse("total is 0 for now");
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
        try {

            this.artist = body.getString("artist");
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
}
