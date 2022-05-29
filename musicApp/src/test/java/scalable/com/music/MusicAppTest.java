package scalable.com.music;


import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testng.annotations.AfterTest;
import scalable.com.music.commands.*;
import scalable.com.music.constants.DatabaseConstants;
import scalable.com.shared.classes.Arango;

import javax.ejb.AfterCompletion;
import java.lang.reflect.Array;

public class MusicAppTest extends MusicTest {
    public static String user_id_1="1";
    public static boolean is_artist_1=false;

    public static String user_id_2="2";
    public static boolean is_artist_2=true;

    public static String track_id = "1";
    public static String playlist_name = "playlist1";
    public static String playlist_id = "3";
    public static String album_id = "3";
    public static String created_playlist1 = "";
    public static String created_playlist2 = "";

    public static JSONObject RequestSimulatorCreatePlaylist(String playlist_name){
        JSONObject body = new JSONObject();
        JSONObject token= new JSONObject();
        token.put("id", "2");
        body.put("name",playlist_name);
        JSONObject request = makeRequest(body, "POST", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new CreatePlaylistCommand().execute(request));
    }

    //playlist ids are randomly generated
    public static JSONObject RequestSimulatorDeletePlaylist(String created_playlist1){
        JSONObject body = new JSONObject();
        JSONObject token= new JSONObject();
        token.put("id", "2");
        body.put("id",created_playlist1);
        JSONObject request = makeRequest(body, "POST", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new DeletePlaylistCommand().execute(request));
    }
    public static JSONObject RequestSimulatorFavoriteAlbum(String album_id){
        JSONObject body = new JSONObject();
        JSONObject token= new JSONObject();
        token.put("id", "2");
        body.put("album_id", album_id);
        JSONObject request = makeRequest(body, "POST", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new FavoriteAlbumCommand().execute(request));
    }
    public static JSONObject RequestSimulatorFavoriteTrack(String track_id){
        JSONObject body = new JSONObject();
        JSONObject token= new JSONObject();
        token.put("id", "1");
        body.put("track_id", track_id);
        JSONObject request = makeRequest(body, "POST", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new FavoriteTrackCommand().execute(request));
    }
    //playlist ids are randomly generated
    public static JSONObject RequestSimulatorPlaylistVisibility(String created_playlist2){
        JSONObject body = new JSONObject();
        JSONObject token= new JSONObject();
        body.put("id", created_playlist2);
        JSONObject request = makeRequest(body, "POST", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new PlaylistVisibilityCommand().execute(request));
    }

    public static JSONObject RequestSimulatorViewFavoriteTracks(){
        JSONObject token= new JSONObject();
        JSONObject body= new JSONObject();
        token.put("id", "1");
        JSONObject request = makeRequest(body, "GET", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new ViewFavoriteTracksCommand().execute(request));
    }

    public static JSONObject RequestSimulatorViewFavoriteAlbums(){
        JSONObject token= new JSONObject();
        JSONObject body= new JSONObject();
        token.put("id", "1");
        JSONObject request = makeRequest(body, "GET", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new ViewFavoriteAlbumsCommand().execute(request));
    }

    public static JSONObject RequestSimulatorFavoriteAlbumsOfOthers(){
        JSONObject uriParams= new JSONObject();
        JSONObject body= new JSONObject();
        JSONObject token= new JSONObject();
        uriParams.put("user_id", "1");
        JSONObject request = makeRequest(body, "GET", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new FavoriteAlbumsOfOthersCommand().execute(request));
    }

    public static JSONObject RequestSimulatorFavoriteTracksOfOthers(){
        JSONObject uriParams= new JSONObject();
        JSONObject token= new JSONObject();
        JSONObject body= new JSONObject();
        uriParams.put("user_id", "1");
        JSONObject request = makeRequest(body, "GET", new JSONObject(),true,token);
        System.out.println("Request " + request.toString());
        return new JSONObject(new FavoriteTracksOfOthersCommand().execute(request));
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
        assert arangoPool.documentExists(DatabaseConstants.DATABASE_NAME,DatabaseConstants.FAVORITE_TRACKS_COLLECTION,user_id_1);
        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("Node User Created Successfully");

    }
    @Test
    public void FavoriteAlbumsOfOthers(){
        JSONObject  response = RequestSimulatorFavoriteAlbumsOfOthers();
        System.out.println(response);
        assert arangoPool.documentExists(DatabaseConstants.DATABASE_NAME,DatabaseConstants.FAVORITE_ALBUMS_COLLECTION,user_id_2);
        assert response.getInt("statusCode") ==200;
        assert response.getString("msg").equals("Node User Created Successfully");
    }
    @Test
    public void CreatePlaylist(){
        JSONObject  response = RequestSimulatorCreatePlaylist(playlist_name);
        System.out.println(response);
        assert arangoPool.documentExists(DatabaseConstants.DATABASE_NAME,DatabaseConstants.PLAYLIST_COLLECTION,playlist_name);

        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("Created Playlist");
    }
    @Test
    public void DeletePlaylist(){
        JSONObject  response = RequestSimulatorDeletePlaylist(created_playlist1);
        System.out.println(response);
        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("Deleted Playlist");
    }
    @Test
    public void FavoriteAlbum(){
        JSONObject  response = RequestSimulatorFavoriteAlbum(album_id);
        System.out.println(response);
        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("Added Album to Favorites");
    }

    @Test
    public void FavoriteTrack(){
        JSONObject  response = RequestSimulatorFavoriteTrack(track_id);
        System.out.println(response);
        assert response.getInt("statusCode") ==200 ;
        assert response.getString("msg").equals("Added Track to Favorites");
    }

    @Test
    public void PlaylistVisibility(){
        JSONObject  response = RequestSimulatorPlaylistVisibility(playlist_id);
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
        JSONObject favorite = RequestSimulatorFavoriteTrack(track_id);
        JSONObject  response = RequestSimulatorViewFavoriteTracks();
        JSONObject data = new JSONObject();
        JSONObject correct = new JSONObject();
        String[] tracks = {"1"};
        data.put("track_id", tracks);
        correct.put("data", data);
        System.out.println(response.getJSONObject("data"));
        System.out.println(data);
        assert response.getInt("statusCode") ==200 ;
        assert response.getJSONObject("data").toString().equals(data.toString());
    }
    @AfterClass
    public static void dropAllCollections() {
        System.out.println("In after test");
        arangoPool.dropCollection(DatabaseConstants.DATABASE_NAME,DatabaseConstants.PLAYLIST_COLLECTION);
        arangoPool.dropCollection(DatabaseConstants.DATABASE_NAME,DatabaseConstants.FAVORITE_ALBUMS_COLLECTION);
        arangoPool.dropCollection(DatabaseConstants.DATABASE_NAME,DatabaseConstants.FAVORITE_TRACKS_COLLECTION);

    }
}