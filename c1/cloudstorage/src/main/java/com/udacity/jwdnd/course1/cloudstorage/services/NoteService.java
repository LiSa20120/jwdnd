package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    Logger logger = LoggerFactory.getLogger(NoteService.class);
    @Autowired
    NoteMapper noteMapper;

    public List<Note> getAllNotes(Integer userId) {
        return noteMapper.getAllNotes(userId);
    }

    public int addNewNote(Note note) {
        var created = -1;
        try {
            created = noteMapper.createNote(note);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return created;
    }

    public int updateNote(Note note) {
        var updated = -1;
        try {
            updated = noteMapper.updateNote(note);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return updated;
    }

    public int deleteNote(Integer noteId) {
        var deleted = -1;
        try {
            noteMapper.deleteNote(noteId);
            deleted = 1;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return deleted;
    }
}
