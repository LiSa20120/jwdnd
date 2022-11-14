package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.entities.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userid=#{userId}")
    List<File> getAllFiles(Integer userId);

    @Select("SELECT * FROM FILES WHERE filename=#{fileName}")
    File getFileByFileName(String fileName);

    @Select("SELECT * FROM FILES WHERE fileid=#{fileId}")
    File getFileById(Integer fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(keyProperty = "fileId", useGeneratedKeys = true)
    int uploadFile(File file);

    @Delete("DELETE FROM FILES WHERE fileid=#{fileId}")
    void deleteFile(Integer fileId);
}
