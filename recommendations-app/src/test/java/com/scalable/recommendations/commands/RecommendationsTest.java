package com.scalable.recommendations.commands;


import org.junit.BeforeClass;
import org.junit.Test;
import scalable.com.shared.classes.Arango;

import static org.junit.Assert.*;
import com.scalable.recommendations.constants.DatabaseConstants;


public abstract class RecommendationsTest {
    public static Arango arangoPool;

    @BeforeClass
    public static void setUp(){
        try {
            arangoPool = Arango.getConnectedInstance();
            arangoPool.createCollectionIfNotExists(DatabaseConstants.DATABASE_NAME, DatabaseConstants.USER_DOCUMENT_COLLECTION, false);
            arangoPool.createCollectionIfNotExists(DatabaseConstants.DATABASE_NAME, DatabaseConstants.MUSIC_DOCUMENT_COLLECTION, false);
            arangoPool.createCollectionIfNotExists(DatabaseConstants.DATABASE_NAME, DatabaseConstants.USER_EDGE_COLLECTION, true);
            arangoPool.createCollectionIfNotExists(DatabaseConstants.DATABASE_NAME, DatabaseConstants.MUSIC_EDGE_COLLECTION, true);
        }
        catch (Exception e){
            fail(e.getMessage());
        }

    }
}
