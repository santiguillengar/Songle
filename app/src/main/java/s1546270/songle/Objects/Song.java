package s1546270.songle.Objects;


import java.io.Serializable;

/**
 * Class used to store a song that may be downloaded for the user to guess and all its information.
 */

public class Song implements Serializable {

    private final int number;
    private final String artist;
    private final String title;
    private final String link;


    public Song(int number, String artist, String title, String link) {
        this.number = number;
        this.artist = artist;
        this.title = title;
        this.link = link;
    }


    public int getNumber() {
        return number;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }


}
