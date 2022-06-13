package me.uwu;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.uwu.struct.Music;
import me.uwu.struct.SearchResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Searcher {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static SearchResult[] searchTrack(String trackName) {


        Request request = new Request.Builder()
                .url("http://ws.audioscrobbler.com/2.0/?method=track.search&track=" + trackName + "&api_key=" + Vars.fmApiKey + "&format=json")
                .get()
                .addHeader("User-Agent", "UwUDev")
                .build();

        try {
            return gson.fromJson(gson.fromJson(client.newCall(request).execute().body().string(), JsonObject.class)
                    .get("results").getAsJsonObject()
                    .get("trackmatches").getAsJsonObject()
                    .get("track"), SearchResult[].class);
        } catch (Exception e) {
            return new SearchResult[0];
        }
    }

    public static String getYouTubeUrl(String title, String artist) {
        Request request = new Request.Builder()
                .url("https://youtube.googleapis.com/youtube/v3/search?part=snippet&q=" + title + " " + artist + "&key=" + Vars.ytApiKey + "&maxResults=1")
                .get()
                .build();

        try {
            return "https://www.youtube.com/watch?v=" + gson.fromJson(client.newCall(request).execute().body().string(), JsonObject.class)
                    .get("items").getAsJsonArray().get(0).getAsJsonObject()
                    .get("id").getAsJsonObject()
                    .get("videoId").getAsString();
        } catch (Exception e) {
            return null;
        }
    }

    public static Music getTrack(String title, String artist) {
        Request request = new Request.Builder()
                .url("http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=" + Vars.fmApiKey + "&artist=" + artist + "&track=" + title + "&format=json")
                .get()
                .addHeader("User-Agent", "UwUDev")
                .build();

        try {
            return gson.fromJson(gson.fromJson(client.newCall(request).execute().body().string(), JsonObject.class).get("track").getAsJsonObject(), Music.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}