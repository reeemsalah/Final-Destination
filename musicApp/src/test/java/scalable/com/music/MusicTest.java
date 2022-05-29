package scalable.com.music;

import org.junit.BeforeClass;
import org.junit.Test;
import scalable.com.shared.classes.Arango;

import static org.junit.Assert.*;
import scalable.com.music.constants.DatabaseConstants;


public abstract class MusicTest {
    public static Arango arangoPool;

    @BeforeClass
    public static void setUp(){
        try {
            arangoPool = Arango.getConnectedInstance();
            arangoPool.createCollectionIfNotExists(DatabaseConstants.DATABASE_NAME, DatabaseConstants.PLAYLIST_COLLECTION,false);
            arangoPool.createCollectionIfNotExists(DatabaseConstants.DATABASE_NAME, DatabaseConstants.FAVORITE_ALBUMS_COLLECTION, false);
            arangoPool.createCollectionIfNotExists(DatabaseConstants.DATABASE_NAME, DatabaseConstants.FAVORITE_TRACKS_COLLECTION, false);
            //arangoPool.createCollectionIfNotExists(DatabaseConstants.DATABASE_NAME, DatabaseConstants.MUSIC_EDGE_COLLECTION, true);
        }
        catch (Exception e){
            fail(e.getMessage());
        }

    }
}