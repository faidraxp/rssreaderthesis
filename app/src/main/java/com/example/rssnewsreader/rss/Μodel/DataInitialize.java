package com.example.rssnewsreader.rss.Μodel;
import java.io.Serializable;

public class DataInitialize implements Serializable{

    int id;
    String link,searchName;

    public DataInitialize() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
}
