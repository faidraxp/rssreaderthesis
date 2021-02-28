package com.example.rssnewsreader.rss.Μodel;

import java.sql.Timestamp;

public class UserReads {
    String fbID;
    Item item;
    Long time;
    

    public UserReads() {
    }

//    public UserReads(String fbID, Item item, Timestamp time) {
//        this.fbID = fbID;
//        this.item = item;
//        this.time = time;
//    }

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
