package com.scalable.notifications;

import scalable.com.shared.App;
import scalable.com.shared.classes.Arango;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NotificationsApp extends App {
    public static Arango arangoPool;

    public static void main(String[] args) throws IOException, ClassNotFoundException, TimeoutException {
        NotificationsApp app = new NotificationsApp();
        app.start();
        Arango arango = Arango.getInstance();
        arango.createDatabaseIfNotExists("NotificationsDB");
        arango.createCollectionIfNotExists("NotificationsDB","Notifications",false);
        arango.createCollectionIfNotExists("NotificationsDB","UserSubscribedTopics",false);
    }


    @Override
    protected String getAppName() {
        return "Notifications";
    }
}
