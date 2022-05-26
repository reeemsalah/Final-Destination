import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FireBaseService {


    FirebaseDatabase db;

    public FireBaseService() throws IOException {
        File file = new File(
                getClass().getClassLoader().getResource("key.json").getFile());

        FileInputStream fis = new FileInputStream(file);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(fis))
//                .setDatabaseUrl("https://restaurants-3bb3e.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);

//        db = FirebaseDatabase.getInstance();
        Firestore db = FirestoreClient.getFirestore();
    }

    public FirebaseDatabase getDb() {
        return db;
    }

}