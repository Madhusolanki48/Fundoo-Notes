package com.fundoonotes.repository;

import java.util.List;
import java.util.Optional;

import com.fundoonotes.entity.Note;
import com.fundoonotes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer> {

	List<Note> findByOwnerAndIsDeletedFalse(User owner);

	List<Note> findByOwnerAndIsArchivedTrueAndIsDeletedFalse(User owner);

	List<Note> findByOwnerAndIsDeletedTrue(User owner);

	Optional<Note> findByNoteIdAndOwner(int noteId, User owner);
}
