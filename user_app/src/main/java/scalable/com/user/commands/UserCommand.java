package scalable.com.user.commands;

import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.CommandVerifier;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class UserCommand extends CommandVerifier {


    @Override
    public void validateAttributesNumber() throws IOException, ValidationException {

        Properties prop = new Properties();
        prop.load(Login.class.getClassLoader().getResourceAsStream(this.getCommandName() + ".properties"));
        for (Map.Entry<Object, Object> e : prop.entrySet()) {
            if (!body.has(e.toString())) {
                throw new ValidationException("Attribute: "+e.toString()+" is missing");
            }
        }

     

       
    }

}
