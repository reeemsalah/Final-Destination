package scalable.com.user.commands;

import com.arangodb.entity.BaseDocument;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends UserCommand  {
     private String username;
     private String password;
     static int count=0;
    @Override
    public String getCommandName() {
        return "SignUp";
    }

    @Override
    public String execute() {
        System.out.println("iam in execute");
        Arango arango=Arango.getInstance();
        arango.createCollectionIfNotExists("spotifyArangoDb","user",false);
        Map<String,Object> attributes=new HashMap<String,Object>();
        attributes.put("username",username);
        attributes.put("password",password);
        BaseDocument baseDocument=new BaseDocument(attributes);
        
        arango.createDocument("spotifyArangoDb","user",baseDocument);

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

        }
        catch (Exception e){
            throw new ValidationException("attributes data types are wrong");
        }
        this.validateAnnotations();
    }
}
