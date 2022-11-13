package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    UserService userService;
    @Autowired
    NoteService noteService;

    @GetMapping
    public String homeView(Authentication auth, Model model) {
        var userName = auth.getName();
        if(userName != null) {
            var user = userService.getUserByUserName(userName);
            if(user != null) {
                List<Note> notes = noteService.getAllNotes(user.getUserid());
                model.addAttribute("notes", notes);
            }
        }
        return "home";
    }

    @PostMapping("/notes")
    public String saveNote(Authentication auth, @ModelAttribute("note") Note note, Model model) {
        note.setUserId(userService.getUserByUserName(auth.getName()).getUserid());
        int saved = -1;
        if (note.getNoteId() != null) {
            saved = noteService.updateNote(note);
        } else {
            saved = noteService.addNewNote(note);
        }

        if (saved < 0) {
            model.addAttribute("saveNoteError", true);
        } else {
            model.addAttribute("saveNoteSuccess", true);
        }
        return "result";
    }

    @GetMapping("/notes/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, Model model) {
        int deleted = -1;
        deleted = noteService.deleteNote(noteId);
        if(deleted < 0) {
            model.addAttribute("deleteNoteError", true);
        } else {
            model.addAttribute("deleteNoteSuccess", true);
        }
        return "result";
    }
}

