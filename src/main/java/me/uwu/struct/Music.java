package me.uwu.struct;

import lombok.Data;

public @Data class Music {
    private final Track track;
    private final int listeners, playcount;
    private final Artist artist;
    private final Album album;
}
