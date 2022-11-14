package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.entities.File;
import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    UserService userService;
    @Autowired
    NoteService noteService;
    @Autowired
    FileService fileService;

    @GetMapping
    public String homeView(Authentication auth, Model model) {
        var userName = auth.getName();
        if(userName != null) {
            var user = userService.getUserByUserName(userName);
            if(user != null) {
                List<Note> notes = noteService.getAllNotes(user.getUserid());
                List<File> files = fileService.getAllFiles(user.getUserid());
                model.addAttribute("notes", notes);
                model.addAttribute("files", files);
            }
        }
        return "home";
    }

    @PostMapping("/notes")
    public String saveNote(Authentication auth, @ModelAttribute("note") Note note, Model model) {
        note.setUserId(userService.getUserByUserName(auth.getName()).getUserid());
        var saved = -1;
        if (note.getNoteId() != null) {
            saved = noteService.updateNote(note);
        } else {
            saved = noteService.addNewNote(note);
        }

        if (saved < 0) {
            logger.error("Could not save note");
            model.addAttribute("saveNoteError", true);
        } else {
            logger.info("Saved note successfully");
            model.addAttribute("saveNoteSuccess", true);
        }
        return "result";
    }

    @GetMapping("/notes/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, Model model) {
        var deleted = -1;
        deleted = noteService.deleteNote(noteId);
        if(deleted < 0) {
            logger.error("Could not delete note with id = {}", noteId);
            model.addAttribute("deleteNoteError", true);
        } else {
            logger.info("Deleted note successfully with id = {}", noteId);
            model.addAttribute("deleteNoteSuccess", true);
        }
        return "result";
    }

    @PostMapping("/files")
    public String uploadFile(Authentication auth, @ModelAttribute("fileUpload") MultipartFile fileUpload, Model model) throws IOException {
        var fileName = fileUpload.getOriginalFilename();
        var fileSize = fileUpload.getSize();
        if(fileName != null && fileSize != 0) {
            if(fileService.isFileNameAvailable(fileName)) {
                var file = new File();
                file.setFileName(fileUpload.getOriginalFilename());
                file.setContentType(fileUpload.getContentType());
                file.setFileSize(Long.toString(fileUpload.getSize()));
                file.setFileData(fileUpload.getBytes());
                file.setUserId(userService.getUserByUserName(auth.getName()).getUserid());
                try {
                    var uploaded = fileService.uploadFile(file);
                    model.addAttribute("uploadFileSuccess", "File uploaded successfully!");
                    logger.info("File with id = {} uploaded successfully", uploaded);
                } catch (MaxUploadSizeExceededException e){
                    model.addAttribute("errorMessage", "File size more than allowed limit (256KB)");
                } catch (Exception e) {
                    model.addAttribute("uploadFileError", "Could not upload file. Something went wrong. Try again!");
                }
            } else {
                model.addAttribute("errorMessage", "File name already exists!");
            }
        } else {
            model.addAttribute("errorMessage", "File name is empty!");
        }
        return "result";
    }

    @GetMapping("/files/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId, Model model) {
        var deleted = -1;
        deleted = fileService.deleteFile(fileId);
        if(deleted < 0) {
            logger.info("Could not delete file");
            model.addAttribute("deleteFileError", true);
        } else {
            logger.info("Deleted file successfully");
            model.addAttribute("deleteFileSuccess", true);
        }
        return "result";
    }

    @GetMapping("/files/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> viewFile(@PathVariable("fileId") Integer fileId) {
        var file = fileService.getFileById(fileId);
        var resource = new ByteArrayResource(file.getFileData());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + file.getFileName() + "\"").body(resource);
    }
}

