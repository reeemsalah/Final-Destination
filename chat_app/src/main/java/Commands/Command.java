package Commands;

import com.google.cloud.firestore.Firestore;

import java.util.concurrent.ExecutionException;

public abstract class Command {
    Firestore db;

    public void execute() throws ExecutionException, InterruptedException {

    }

    public void respond(){

    }
}
