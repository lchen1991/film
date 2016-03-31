package com.filmresource.cn.bean;

/**
 * Created by ql on 2016/3/31.
 */
public class TorrentFileInfo extends BaseEntity {

    private String fileName;
    private String filePath;
    private String fileCreateTime;

    public String getFilePath() {
        return filePath;
    }

    public TorrentFileInfo setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public TorrentFileInfo setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileCreateTime() {
        return fileCreateTime;
    }

    public TorrentFileInfo setFileCreateTime(String fileCreateTime) {
        this.fileCreateTime = fileCreateTime;
        return this;
    }
}
