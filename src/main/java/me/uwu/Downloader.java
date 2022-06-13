package me.uwu;

import java.io.IOException;

public class Downloader {
    public static boolean download(String url, String fileName) {
        ProcessBuilder pb = new ProcessBuilder(
                "yt-dlp",
                "--extract-audio",
                "--audio-format", "mp3",
                "--audio-quality", "0",
                "--output", fileName,
                url
        );
        try {
            pb.start().waitFor();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
