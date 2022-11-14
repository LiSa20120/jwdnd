package com.udacity.jwdnd.course1.cloudstorage.entities;

import lombok.Data;

@Data
public class File {
    private Integer fileId;
    private String fileName;
    private String contentType;
    private String fileSize;
    private Integer userId;
    private byte[] fileData;
    private String fileUrl;
}
