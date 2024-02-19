package dev.nyvil.autofix.client.notifications;

public class Notification {

    private String title;
    private String subtitle;
    private long fadeInDuration;
    private long displayDuration;
    private long fadeOutDuration;
    private int backgroundColor;
    private int titleColor;
    private int subtitleColor;
    private long creationTime;
    private long lifetime;


    public Notification(String title, String subtitle, long fadeInDuration, long displayDuration, long fadeOutDuration, int backgroundColor, int titleColor, int subtitleColor) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeInDuration = fadeInDuration;
        this.displayDuration = displayDuration;
        this.fadeOutDuration = fadeOutDuration;
        this.backgroundColor = backgroundColor;
        this.titleColor = titleColor;
        this.subtitleColor = subtitleColor;
        this.creationTime = System.currentTimeMillis();
        this.lifetime = fadeInDuration + displayDuration + fadeOutDuration;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public long getFadeInDuration() {
        return fadeInDuration;
    }

    public void setFadeInDuration(long fadeInDuration) {
        this.fadeInDuration = fadeInDuration;
    }

    public long getDisplayDuration() {
        return displayDuration;
    }

    public void setDisplayDuration(long displayDuration) {
        this.displayDuration = displayDuration;
    }

    public long getFadeOutDuration() {
        return fadeOutDuration;
    }

    public void setFadeOutDuration(long fadeOutDuration) {
        this.fadeOutDuration = fadeOutDuration;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public int getSubtitleColor() {
        return subtitleColor;
    }

    public void setSubtitleColor(int subtitleColor) {
        this.subtitleColor = subtitleColor;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLifetime() {
        return lifetime;
    }

    public void setLifetime(long lifetime) {
        this.lifetime = lifetime;
    }
}
