package scalable.com.music.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.arangodb.entity.BaseDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import scalable.com.shared.classes.Arango;
import scalable.com.music.MusicApp;

public class MusicTest {
    private String artist_id = "1", user_id ="1", song_id = "1";
    private static Arango arango;
    @BeforeClass
    public static void setUp() {
        try {
            arango = Arango.getConnectedInstance();
            MusicApp app= new MusicApp();
            app.dbInit();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public static String createSong(String name, String artists, String genres, String album_id, boolean isArtist) {
        JSONObject body = new JSONObject();
        body.put("name", name);
        body.put("artists", artists);
        body.put("genres", genres);
        body.put("album_id", album_id);

        JSONObject uriParams = new JSONObject();
        JSONObject files = new JSONObject();
        JSONObject dummyData = new JSONObject();
        dummyData.put("data","FILE DATA");
        dummyData.put("type","FILE TYPE");
        files.put("song", dummyData);
        files.put("cover", dummyData);
        JSONObject token= new JSONObject();
        if(isArtist)
        token.put("isArtist", "true");
        else
        token.put("isArtist", "false");
        JSONObject request = new JSONObject();
        request.put("body", body);
        request.put("methodType", "POST");
        request.put("uriParams", uriParams);
        request.put("isAuthenticated",true);
        request.put("tokenPayload", token);
        request.put("files", files);

        CreateSongCommand createSong= new CreateSongCommand();
        return createSong.execute(request);
    }
    public static String getSongNumberOfStreams(String song_id) {
        JSONObject body = new JSONObject();
        body.put("song_id", song_id);

        JSONObject uriParams = new JSONObject();
        JSONObject token= new JSONObject();
        JSONObject request = new JSONObject();
        request.put("body", body);
        request.put("methodType", "POST");
        request.put("uriParams", uriParams);
        request.put("isAuthenticated",false);
        request.put("tokenPayload", token);

        GetSongNumberOfStreams getSongNumberOfStreams= new GetSongNumberOfStreams();
        return getSongNumberOfStreams.execute(request);
    }
    public static String getArtistNumberOfStreams(String artist_id) {
        JSONObject body = new JSONObject();
        body.put("artist_id", artist_id);

        JSONObject uriParams = new JSONObject();
        JSONObject token= new JSONObject();
        JSONObject request = new JSONObject();
        request.put("body", body);
        request.put("methodType", "POST");
        request.put("uriParams", uriParams);
        request.put("isAuthenticated",false);
        request.put("tokenPayload", token);

        GetArtistNumberOfStreams getArtistNumberOfStreams= new GetArtistNumberOfStreams();
        return getArtistNumberOfStreams.execute(request);
    }

    @Test
    public void createSongAsArtistTest() {
        int beforeCount= arango.documentCount("Spotify", "Songs");
        String response = createSong("Song1", "1,2","Rock,Pop","1",true);
        JSONObject responseJson = new JSONObject(response);
        int afterCount =arango.documentCount("Spotify", "Songs");
        assertEquals("Song created!", responseJson.getString("msg"));
        assertEquals(beforeCount+1, afterCount);
    }
    @Test
    public void createSongAsNonArtistTest() {
        int beforeCount= arango.documentCount("Spotify", "Songs");
        String response = createSong("Song1", "1,2","Rock,Pop","1",false);
        JSONObject responseJson = new JSONObject(response);
        int afterCount =arango.documentCount("Spotify", "Songs");
        assertEquals("Only artists can create songs", responseJson.getString("msg"));
        assertEquals(beforeCount, afterCount);
    }
    @Test
    public void getSongNumberOfStreamsTest() {
        String response = getSongNumberOfStreams(song_id);
        JSONObject responseJson = new JSONObject(response);
        BaseDocument songExist = arango.readDocument("Spotify","Songs",song_id);
        if(songExist==null){
            assertEquals(404, responseJson.getInt("statusCode"));

        }   else{
            assertEquals(200, responseJson.getInt("statusCode"));

        }
    }
    @Test
    public void getArtistNumberOfStreamsTest() {
        String response = getArtistNumberOfStreams(artist_id);
        JSONObject responseJson = new JSONObject(response);
        assertEquals(200, responseJson.getInt("statusCode"));
    }

}