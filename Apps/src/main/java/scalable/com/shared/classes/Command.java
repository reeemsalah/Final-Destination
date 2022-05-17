package scalable.com.shared.classes;

import org.json.JSONObject;

public abstract class Command {
     public abstract String getCommandName();
    public abstract String execute(JSONObject request);
    
}
