package me.uwu.struct;

import com.google.gson.JsonObject;
import lombok.Data;

public @Data class Album {
    private final String artist, title;
    private final JsonObject[] image;

    public String getBestImageUrl() {
        return image[image.length - 1].get("#text").getAsString();
    }
}
