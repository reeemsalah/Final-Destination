package Commands;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateChatCommand extends Command {
    //the add Document Command

    private String docName;
    @Override
    public void execute() throws ExecutionException, InterruptedException {
        //TODO: Here we just need to add a document and we will specify its name
        super.execute();
        DocumentReference docRef = db.collection("users").document(docName);
        Map<String, Object> data = new HashMap<>();
        data.put("first", "Ada");
        ApiFuture<WriteResult> result = docRef.set(data);
        System.out.println("Update time : " + result.get().getUpdateTime());
    }
}
