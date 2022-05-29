package scalable.com.user.commands;

import org.json.JSONObject;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.*;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
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
    @NotBlank(message="email should not be empty")

      private String email;
    @NotBlank(message="password should not be empty")
      private String password;
    
     


    @Override
    public String execute() {

        
        Connection connection = null;
        ResultSet result=null;
        PreparedStatement preparedStatement=null;
        String username= null;
        String last_name=null;
        String first_name=null;
        String image_url=null;
        String id=null;
        boolean isArtist;
        JSONObject response=new JSONObject();
        try {

            //String sqlStatement = String.format(,DatabaseTablesNames.Postgres_USERS_TABLE_NAME,this.email,this.password);
            //System.out.println(sqlStatement);
            connection = PostgresConnection.getDataSource().getConnection();
            connection.setAutoCommit(true);
            
            preparedStatement=connection.prepareStatement("select id,username,first_name,last_name,profile_photo,password,isartist from users where email=?", ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
           
            preparedStatement.setString(1,this.email);
            
            System.out.println("before executing");
            result=preparedStatement.executeQuery();
            System.out.println("after executing");
            //System.out.println(result.getFetchSize());
               result.last();
            if(result.getRow()>=1){
                   String passwordHash=result.getString("password");
                   if(ByCryptHelper.verifyHash(this.password,passwordHash)) {
                       username = result.getString("username");
                       last_name = result.getString("last_name");
                       first_name = result.getString("first_name");
                       image_url = result.getString("profile_photo");
                       isArtist=result.getBoolean("isartist");

                       id = result.getString("id");

                       Map<String, String> claims = new HashMap<String, String>();
                       claims.put("id", id);
                       claims.put("isArtist",""+isArtist);

                       String token = JWTHandler.generateToken(claims);

                       response.put("username", username);
                       response.put("last_name", last_name);
                       response.put("first_name", first_name);
                       response.put("id", id);
                       if (image_url != null) response.put("image_url", image_url);
                       response.put("token", token);

                   }else{
                       return Responder.makeErrorResponse("wrong username or password",406);
                   }
                
            }  else{
                     return Responder.makeErrorResponse("wrong username or password",406);
            }




        } catch (SQLException e) {
            System.out.println(e.toString());
            return  Responder.makeErrorResponse("Something went wrong",400);
        } finally {

            PostgresConnection.disconnect(result, preparedStatement, connection);
        }
        return Responder.makeDataResponse(response);
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

            this.email = body.getString("email");
            this.password = body.getString("password");
        }
        catch (Exception e){
            throw new ValidationException("attributes data types are wrong: "+e.getMessage());
        }
       

    }

    @Override
    public String getCommandName() {
        return "Login";
    }
}
