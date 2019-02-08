package com.presidentio.freelance.hadoopproject9336419;

/**
 * Created by presidentio on 1/18/16.
 */
public class ResultItem {

    private String language;

    private long position;

    private String hashTag;

    private long timeStart;

    private long timeEnd;

    public ResultItem() {
    }

    public ResultItem(String language, long position, String hashTag, long timeStart, long timeEnd) {
        this.language = language;
        this.position = position;
        this.hashTag = hashTag;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getLanguage() {
        return language;
    }

    public ResultItem setLanguage(String language) {
        this.language = language;
        return this;
    }

    public long getPosition() {
        return position;
    }

    public ResultItem setPosition(long position) {
        this.position = position;
        return this;
    }

    public String getHashTag() {
        return hashTag;
    }

    public ResultItem setHashTag(String hashTag) {
        this.hashTag = hashTag;
        return this;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public ResultItem setTimeStart(long timeStart) {
        this.timeStart = timeStart;
        return this;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public ResultItem setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
        return this;
    }
}
