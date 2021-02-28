package com.example.rssnewsreader.rss.Μodel;
import java.io.Serializable;
public class Enclosure implements Serializable {
    private String link;

    public Enclosure() {
    }

    public Enclosure(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
