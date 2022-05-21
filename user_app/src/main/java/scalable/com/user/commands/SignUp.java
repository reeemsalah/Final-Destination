package scalable.com.user.commands;

import com.arangodb.entity.BaseDocument;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.PostgresConnection;
import scalable.com.shared.classes.Responder;

import javax.validation.constraints.NotBlank;
import java.sql.*;
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
    
     private boolean isArtist;
     private boolean isPremium;
    @Override
    public String getCommandName() {
        return "SignUp";
    }

    @Override
    public String execute() {


        System.out.println("iam in execute");
        //cases 1- username already exists 2- email already exists


        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            // call userregister(username=>'fady',email=>'fady@guc.com',password=>'123',first_name=>'fady',last_name=>'aziz',isartist=>true)
            String procCall = "call userregister(?,?,?,?,?,?)";
            connection = PostgresConnection.getDataSource().getConnection();
            connection.setAutoCommit(true);

            preparedStatement = connection.prepareStatement(procCall);
            preparedStatement.setString(1, this.username);
            preparedStatement.setString(2, this.email);
            preparedStatement.setString(3, this.password);
            preparedStatement.setString(4, this.firstName);
            preparedStatement.setString(5, this.lastName);
            preparedStatement.setBoolean(6, this.isArtist);


            preparedStatement.executeUpdate();
             return Responder.makeMsgResponse("successfully registered!!!");
            //System.out.println(result.first());
        } catch (SQLException e) {
            if (e.getMessage().contains("(username)"))
                return Responder.makeErrorResponse("username already exists", 406);
            if (e.getMessage().contains("(email)"))
                return Responder.makeErrorResponse("email already registerd", 406);
            System.out.println("here" + e.getMessage());
        } finally {

            PostgresConnection.disconnect(null, preparedStatement, connection);
        }
        //System.out.println("db " + dbConn);

        try {
            Arango arango = Arango.getInstance();
            arango.createCollectionIfNotExists("spotifyArangoDb", "user", false);
            Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put("username", username);
            attributes.put("password", password);
            BaseDocument baseDocument = new BaseDocument(attributes);

            arango.createDocument("spotifyArangoDb", "user", baseDocument);

            return null;
        }catch (Exception e){
             System.out.println(e.getMessage());
             Responder.makeErrorResponse(e.getMessage(),400);
        }
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
