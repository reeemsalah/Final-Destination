package scalable.com.user.commands;

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
import javax.validation.constraints.Min;
public class Login extends UserCommand{
    @NotBlank(message="username should not be empty")
      private String username;
    @NotBlank(message="password should not be empty")
      private String password;
      


    @Override
    public String execute() {
       
        return Responder.makeMsgResponse("wala wala wala, el so7ab yala");
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
        try {

            this.username = body.getString("username");
            this.password = body.getString("password");
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
        return "Login";
    }
}
