package main.tal.db;

public class TwitterLink {
    private String track;
    private String title;
    private String content;
    private String url;
    private String imageUrl;
    private String description;

    public TwitterLink() {}

    public void balanceNulls() {
        if(track == null)
            track = "";
        if(title == null)
            title = "";
        if(content == null)
            content = "";
        if(url == null)
            url = "";
        if(imageUrl == null)
            imageUrl = "";
        if(description == null)
            description = "";
    }

    public String getTrack() { return track; }

    public void setTrack(String track) { this.track = track; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
