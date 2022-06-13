package me.uwu.struct;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

public @Data class Track {
    @SerializedName("name")
    private final String title;
    private final int duration;
}
