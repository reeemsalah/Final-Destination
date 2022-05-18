package scalable.com.user.commands;

import com.arangodb.entity.BaseDocument;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends UserCommand  {
    @NotBlank(message = "username should not be empty")
    private String username;
    @NotBlank(message = "password should not be empty")
     private String password;
    @NotBlank(message = "email should not be empty")
     private String email;
    @NotBlank(message = "first name should not be empty")
     private String firstName;
    @NotBlank(message = "last name should not be empty")
     private String lastName;
    @NotBlank(message = "isArtist should not be empty")
     private boolean isArtist;
     private boolean isPremium;
    @Override
    public String getCommandName() {
        return "SignUp";
    }

    @Override
    public String execute() {

        

        System.out.println("iam in execute");
        


//        Arango arango=Arango.getInstance();
//        arango.createCollectionIfNotExists("spotifyArangoDb","user",false);
//        Map<String,Object> attributes=new HashMap<String,Object>();
//        attributes.put("username",username);
//        attributes.put("password",password);
//        BaseDocument baseDocument=new BaseDocument(attributes);
//
//        arango.createDocument("spotifyArangoDb","user",baseDocument);

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
        try {
            System.out.println("iam in validate");
            this.username = body.getString("username");
            this.password = body.getString("password");
            this.email=body.getString("email");
            this.firstName= body.getString("firstname");
            this.lastName=body.getString("lastname");
            this.isArtist=body.getBoolean("isArtist");
            this.isPremium=false;
        }
        catch (Exception e){
            throw new ValidationException("attributes data types are wrong: "+e.getMessage());
        }
        
    }
}
