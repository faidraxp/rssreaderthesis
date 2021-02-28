package com.example.rssnewsreader.rss.Μodel;

import java.util.List;
import java.io.Serializable;
public class Item implements Serializable,Comparable<Item>{

    private String id;
    private String title;
    private String pubDate;
    private String link;
    private String guid;
    private String author;
    private String thumbnail;
    private String description;
    private String content;
    private Enclosure enclosure;
    private List<String> categories;

    public Item() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Enclosure getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }


    @Override
    public int compareTo(Item item){
        if (getPubDate() == null || item.getPubDate() == null) {
            return 0;
        }
        return getPubDate().compareTo(item.getPubDate());
    }

}
