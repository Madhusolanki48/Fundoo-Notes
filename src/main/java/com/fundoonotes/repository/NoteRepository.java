package com.fundoonotes.repository;

import java.util.List;
import java.util.Optional;

import com.fundoonotes.entity.Note;
import com.fundoonotes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NoteRepository extends JpaRepository<Note, Integer>, JpaSpecificationExecutor<Note> {

	List<Note> findByOwnerAndIsDeletedFalse(User owner);

	List<Note> findByOwnerAndIsArchivedTrueAndIsDeletedFalse(User owner);

	List<Note> findByOwnerAndIsDeletedTrue(User owner);

	List<Note> findByOwnerAndIsDeletedFalseAndRemindersIsNotEmpty(User owner);

	List<Note> findByOwnerAndLabelsLabelIgnoreCaseAndIsDeletedFalse(User owner, String label);

	Optional<Note> findByNoteIdAndOwner(int noteId, User owner);
}
