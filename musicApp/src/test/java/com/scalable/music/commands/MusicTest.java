
import org.junit.BeforeClass;
import org.junit.Test;
import scalable.com.shared.classes.Arango;

import static org.junit.Assert.*;
import com.scalable.recommendations.constants.DatabaseConstants;


public abstract class MusicTest {
    public static Arango arangoPool;

    @BeforeClass
    public static void setUp(){
        try {
            arangoPool = Arango.getConnectedInstance();
            arangoPool.createCollectionIfNotExists(DatabaseConstants.DATABASE_NAME, DatabaseConstants.Playlists, false);
            arangoPool.createCollectionIfNotExists(DatabaseConstants.DATABASE_NAME, DatabaseConstants.FavoriteAlbums, false);
            arangoPool.createCollectionIfNotExists(DatabaseConstants.DATABASE_NAME, DatabaseConstants.FavoriteTracks, false);
            //arangoPool.createCollectionIfNotExists(DatabaseConstants.DATABASE_NAME, DatabaseConstants.MUSIC_EDGE_COLLECTION, true);
        }
        catch (Exception e){
            fail(e.getMessage());
        }

    }
}