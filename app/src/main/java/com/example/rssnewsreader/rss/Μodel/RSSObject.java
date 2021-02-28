package com.example.rssnewsreader.rss.Μodel;

import java.util.List;
import java.io.Serializable;
public class RSSObject implements Serializable{

    public String status;
    public Feed feed;
    public List<Item> items;

    public RSSObject() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
