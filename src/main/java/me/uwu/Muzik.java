package me.uwu;


import com.mpatric.mp3agic.*;
import me.uwu.struct.Music;
import me.uwu.struct.SearchResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Muzik {
    public static void main(String[] args) {
        /*String url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        String fileName = "test.mp3";
        if (Downloader.download(url, fileName)) {
            System.out.println("Downloaded successfully!");
        } else {
            System.out.println("Download failed!");
        }*/
        Scanner scanner = new Scanner(System.in);
        System.out.print("\u001B[32mEnter title\n\u001B[33m> \u001B[35m");
        String title = scanner.nextLine();
        SearchResult[] results = Searcher.searchTrack(title);
        for (int i = 0; i < results.length; i++) {
            System.out.println("\u001B[34mID:\u001B[36m " + i + "   \u001B[34mName:\u001B[36m " + results[i].getName() + "   \u001B[34mArtist:\u001B[36m " + results[i].getArtist());
        }
        System.out.print("\n");
        System.out.print("\u001B[32mEnter ID\n\u001B[33m> \u001B[35m");
        SearchResult selected = results[scanner.nextInt()];

        System.out.println("\u001B[32mScrapping Metadata...\u001B[0m");
        Music track = Searcher.getTrack(selected.getName(), selected.getArtist());
        if (track == null) {
            System.out.println("\u001B[31mTrack metadata not found!\u001B[0m");
            return;
        }
        System.out.println("\u001B[32mMetadata parsed!\u001B[0m");

        String url = Searcher.getYouTubeUrl(selected.getName(), selected.getArtist());
        System.out.println("\u001B[32mDownloading...\u001B[0m");
        File outFolder;
        if (track.getAlbum() != null)
            outFolder = new File("music/" + track.getAlbum().getTitle() + "/");
        else outFolder = new File("music/" + selected.getArtist() + "/");
        if (!outFolder.exists())
            outFolder.mkdirs();

        if (Downloader.download(url, outFolder + "/" + selected.getName() + ".mp3")) {
            System.out.println("\u001B[32mDownloaded successfully!\u001B[0m");
            try {
                System.out.println("\u001B[32mEncoding metadata...\u001B[0m");
                Mp3File mp3file = new Mp3File(outFolder + "/" + selected.getName() + ".mp3");
                ID3v2 id3v2Tag;
                if (mp3file.hasId3v2Tag()) {
                    id3v2Tag = mp3file.getId3v2Tag();
                } else {
                    id3v2Tag = new ID3v24Tag();
                    mp3file.setId3v2Tag(id3v2Tag);
                }
                id3v2Tag.setArtist(selected.getArtist());
                id3v2Tag.setTitle(selected.getName());
                id3v2Tag.setUrl(url);

                if (track.getAlbum() != null) {
                    id3v2Tag.setAlbum(track.getAlbum().getTitle());
                    id3v2Tag.setAlbumArtist(track.getAlbum().getArtist());
                    System.out.println("\u001B[32mDownloading artwork...\u001B[0m");
                    URL artworkUrl = new URL(track.getAlbum().getBestImageUrl());


                    id3v2Tag.setAlbumImage(downloadUrl(artworkUrl), "image/" + track.getAlbum().getBestImageUrl().split("\\.")[1]);
                    System.out.println("\u001B[32mArtwork downloaded and encoded!\u001B[0m");
                }

                mp3file.save(outFolder + "/" + selected.getName() + ".mp3x");
                new File(outFolder + "/" + selected.getName() + ".mp3").delete();
                new File(outFolder + "/" + selected.getName() + ".mp3x").renameTo(new File(outFolder + "/" + selected.getName() + ".mp3"));

            } catch (IOException | UnsupportedTagException | NotSupportedException | InvalidDataException e) {
                System.out.println("\u001B[31mFailed to encode metadata!\u001B[0m");
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("\u001B[31mDownload failed!\u001B[0m");
        }
        System.out.println("\u001B[32mDone!\u001B[0m");
    }

    private static byte[] downloadUrl(URL toDownload) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = toDownload.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputStream.toByteArray();
    }
}
