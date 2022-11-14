package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entities.File;
import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    Logger logger = LoggerFactory.getLogger(FileService.class);
    @Autowired
    FileMapper fileMapper;

    public List<File> getAllFiles(Integer userId) {
        return fileMapper.getAllFiles(userId);
    }

    public boolean isFileNameAvailable(String filename) {
        return fileMapper.getFileByFileName(filename) == null;
    }

    public File getFileByFileName(String fileName) {
        return fileMapper.getFileByFileName(fileName);
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public int uploadFile(File file) {
        var uploaded = -1;
        try {
            uploaded = fileMapper.uploadFile(file);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return uploaded;
    }

    public int deleteFile(Integer fileId) {
        var deleted = -1;
        try {
            fileMapper.deleteFile(fileId);
            deleted = 1;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return deleted;
    }
}
