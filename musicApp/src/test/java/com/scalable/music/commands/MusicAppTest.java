import com.scalable.recommendations.constants.DatabaseConstants;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testng.annotations.AfterTest;
import scalable.com.shared.classes.Arango;

import javax.ejb.AfterCompletion;

public class MusicAppTest extends MusicTest {
    public static String user_id_1="1";
    public static boolean is_artist_1=false;

    public static String user_id_2="2";
    public static boolean is_artist_2=true;

    public static String track_id = "1";
    public static String playlist_name = "playlist1";
    public static String playlist_id = "3";
    public static String album_id = 3;

    public static JSONObject RequestSimulatorCreatePlaylist(String playlist_name){
        JSONObject body = new JSONObject();
        JSONObject token= new JSONObject();
        token.put("id", "2");
        body.put("name",name);
        JSONObject request = makeRequest(body, "POST", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new CreatePlaylist().execute(request));
    }

    //playlist ids are randomly generated
    public static JSONObject RequestSimulatorDeletePlaylist(){
        JSONObject body = new JSONObject();
        JSONObject token= new JSONObject();
        token.put("id", "2");
        body.put("id","3343");
        JSONObject request = makeRequest(body, "POST", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new DeletePlaylist().execute(request));
    }
    public static JSONObject RequestSimulatorFavoriteAlbum(String album_id){
        JSONObject body = new JSONObject();
        JSONObject token= new JSONObject();
        token.put("id", "2");
        body.put("album_id", album_id);
        JSONObject request = makeRequest(body, "POST", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new FavoriteAlbum().execute(request));
    }
    public static JSONObject RequestSimulatorFavoriteTrack(String track_id){
        JSONObject body = new JSONObject();
        JSONObject token= new JSONObject();
        token.put("id", "1");
        body.put("track_id", track_id);
        JSONObject request = makeRequest(body, "POST", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new FavoriteTrack().execute(request));
    }
    //playlist ids are randomly generated
    public static JSONObject RequestSimulatorPlaylistVisibility(String playlist_id){
        JSONObject body = new JSONObject();
        JSONObject token= new JSONObject();
        body.put("id", playlist_id);
        JSONObject request = makeRequest(body, "POST", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new PlaylistVisibility().execute(request));
    }

    public static JSONObject RequestSimulatorViewFavoriteTracks(){
        JSONObject token= new JSONObject();
        token.put("id", "1");
        JSONObject request = makeRequest(body, "GET", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new ViewFavoriteTracks().execute(request));
    }

    public static JSONObject RequestSimulatorViewFavoriteAlbums(){
        JSONObject token= new JSONObject();
        token.put("id", "1");
        JSONObject request = makeRequest(body, "GET", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new ViewFavoriteAlbums().execute(request));
    }

    public static JSONObject RequestSimulatorFavoriteAlbumsOfOthers(){
        JSONObject uriParams= new JSONObject();
        uriParams.put("user_id", "1");
        JSONObject request = makeRequest(body, "GET", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new FavoriteAlbumsOfOthers().execute(request));
    }

    public static JSONObject RequestSimulatorFavoriteTracksOfOthers(){
        JSONObject uriParams= new JSONObject();
        uriParams.put("user_id", "1");
        JSONObject request = makeRequest(body, "GET", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new FavoriteTracksOfOthers().execute(request));
    }
    public static JSONObject makeRequest(JSONObject body, String methodType, JSONObject uriParams, boolean isAuthenticated, JSONObject tokenPayload) {
        JSONObject request = new JSONObject();
        request.put("body", body);
        request.put("methodType", methodType);
        request.put("uriParams", uriParams);
        JSONObject authenticationParams = new JSONObject();
        request.put("isAuthenticated", isAuthenticated);
        request.put("tokenPayload", tokenPayload);

        request.put("authenticationParams", authenticationParams);
        return request;
    }
    @Test
    public void FavoriteTracksOfOthers(){
        JSONObject  response = RequestSimulatorFavoriteTracksOfOthers();
        assert arangoPool.documentExists(DatabaseConstants.DATABASE_NAME,DatabaseConstants.FavoriteTracks,user_id_1);
        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("Node User Created Successfully");

    }
    @Test
    public void FavoriteAlbumsOfOthers(){
        JSONObject  response = RequestSimulatorFavoriteAlbumsOfOthers();
        System.out.println(response);
        assert arangoPool.documentExists(DatabaseConstants.DATABASE_NAME,DatabaseConstants.USER_DOCUMENT_COLLECTION,user_id_2);
        assert response.getInt("statusCode") ==200;
        assert response.getString("msg").equals("Node User Created Successfully");
    }
    @Test
    public void CreatePlaylist(){
        JSONObject  response = RequestSimulatorCreatePlaylist(playlist_name);
        System.out.println(response);
        assert arangoPool.documentExists(DatabaseConstants.DATABASE_NAME,DatabaseConstants.MUSIC_DOCUMENT_COLLECTION,track_id);

        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("Created Playlist");
    }
    @Test
    public void DeletePlaylist(){
        JSONObject  response = RequestSimulatorDeletePlaylist();
        System.out.println(response);
        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("Deleted Playlist");
    }
    @Test
    public void FavoriteAlbum(){
        JSONObject  response = RequestSimulatorFavoriteAlbum(String album_id);
        System.out.println(response);
        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("Added Album to Favorites");
    }

    @Test
    public void FavoriteTrack(){
        JSONObject  response = RequestSimulatorFavoriteTrack(String track_id);
        System.out.println(response);
        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("Added Track to Favorites");
    }

    @Test
    public void PlaylistVisibility(){
        JSONObject  response = RequestSimulatorPlaylistVisibility(String playlist_id);
        System.out.println(response);
        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("UpdatedPlaylist");
    }
    //response is an object
    @Test
    public void ViewFavoriteAlbums(){
        JSONObject  response = RequestSimulatorViewFavoriteAlbums();
        System.out.println(response);
        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("UpdatedPlaylist");
    }
    //response is an object
    @Test
    public void ViewFavoriteTracks(){
        JSONObject  response = RequestSimulatorViewFavoriteTracks();
        System.out.println(response);
        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("UpdatedPlaylist");
    }
    @AfterClass
    public static void dropAllCollections() {
        System.out.println("In after test");
        arangoPool.dropCollection(DatabaseConstants.DATABASE_NAME,DatabaseConstants.USER_DOCUMENT_COLLECTION);
        arangoPool.dropCollection(DatabaseConstants.DATABASE_NAME,DatabaseConstants.MUSIC_DOCUMENT_COLLECTION);
        arangoPool.dropCollection(DatabaseConstants.DATABASE_NAME,DatabaseConstants.USER_EDGE_COLLECTION);
        arangoPool.dropCollection(DatabaseConstants.DATABASE_NAME,DatabaseConstants.MUSIC_EDGE_COLLECTION);

    }
}