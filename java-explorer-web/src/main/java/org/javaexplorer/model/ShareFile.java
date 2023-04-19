package org.javaexplorer.model;

import lombok.Data;

import java.util.List;

@Data
public class ShareFile {
    public static final String REDIS_KEY = "ShareFile";
    private String id;
    private String deletingToken;
    private Integer hoursToLive;
    private List<SrcFile> srcFiles;
}
