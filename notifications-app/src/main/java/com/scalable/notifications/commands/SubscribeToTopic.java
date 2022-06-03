package com.scalable.notifications.commands;

import com.arangodb.entity.BaseDocument;
import com.scalable.notifications.FirebaseMessagingConnector;
import scalable.com.exceptions.ValidationException;
import scalable.com.shared.classes.Arango;
import scalable.com.shared.classes.CommandVerifier;
import scalable.com.shared.classes.Responder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class SubscribeToTopic extends CommandVerifier {
    @NotNull(message="topic should not be empty")
    private String topicToSubscribe;

    @Override
    public String getCommandName() {
        return "SubscribeToTopic";
    }

    @Override
    public String execute() {
        int id=Integer.parseInt(this.tokenPayload.getString("id"));
        Arango arango = null;
        try {
            arango = Arango.getInstance();
            arango.createCollectionIfNotExists("NotificationsDB","UserSubscribedTopics",false);
            BaseDocument doc = new BaseDocument();
            doc.setKey(id+"");
            doc.addAttribute("user_id", id);
            ArrayList<String> topics = new ArrayList<String>();
            if(!arango.documentExists("NotificationsDB","userSubscribedToTopics",id+"")){
                topics.add(this.topicToSubscribe);
                doc.addAttribute("topics", topics);
                BaseDocument res = arango.createDocument("NotificationsDB","UserSubscribedTopics", doc);
                System.out.println("Topic added to DB");
            }
            else{
                BaseDocument docToUpdate = arango.readDocument("NotificationsDB","UserSubscribedTopics", id+"");
                topics = (ArrayList<String>) docToUpdate.getAttribute("topics");
                topics.add(this.topicToSubscribe);
                doc.addAttribute("topics", topics);
                arango.updateDocument("NotificationsDB","userSubscribedToTopics",doc,id+"");
            }
            return Responder.makeMsgResponse("Subscribed to topic successfully");
        } catch (Exception e){
            return Responder.makeErrorResponse(e.getMessage(), 500);
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
            this.topicToSubscribe = body.getString("topic");
        }
        catch (Exception e){
            throw new ValidationException("attributes data types are wrong");
        }
        this.validateAnnotations();
    }
}
