package com.fundoonotes.service;

import java.util.List;

import com.fundoonotes.dto.NoteRequest;
import com.fundoonotes.dto.NoteResponse;
import com.fundoonotes.entity.Note;
import com.fundoonotes.entity.User;
import com.fundoonotes.exception.InvalidNoteStateException;
import com.fundoonotes.exception.NoteNotFoundException;
import com.fundoonotes.repository.NoteRepository;
import com.fundoonotes.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

	private final NoteRepository noteRepository;
	private final UserRepository userRepository;

	public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
		this.noteRepository = noteRepository;
		this.userRepository = userRepository;
	}

	public NoteResponse addNote(NoteRequest request, String email) {
		User user = getUser(email);

		Note note = new Note();
		copyRequestToNote(request, note);
		note.setOwner(user);

		Note savedNote = noteRepository.save(note);
		return new NoteResponse(savedNote);
	}

	public List<NoteResponse> getNotes(String email) {
		User user = getUser(email);
		return noteRepository.findByOwnerAndIsDeletedFalse(user)
				.stream()
				.map(NoteResponse::new)
				.toList();
	}

	public NoteResponse getNote(int noteId, String email) {
		Note note = getNoteForUser(noteId, email);

		if (note.getIsDeleted()) {
			throw new NoteNotFoundException("Note not found");
		}

		return new NoteResponse(note);
	}

	public NoteResponse updateNote(NoteRequest request, String email) {
		Note note = getNoteForUser(request.getNoteId(), email);

		if (note.getIsDeleted()) {
			throw new InvalidNoteStateException("Cannot update a trashed note");
		}

		copyRequestToNote(request, note);
		return new NoteResponse(noteRepository.save(note));
	}

	public NoteResponse pinUnpinNote(int noteId, String email) {
		Note note = getNoteForUser(noteId, email);

		if (note.getIsDeleted()) {
			throw new InvalidNoteStateException("Cannot pin a trashed note");
		}

		note.setIsPined(!note.getIsPined());
		return new NoteResponse(noteRepository.save(note));
	}

	public NoteResponse archiveNote(int noteId, String email) {
		Note note = getNoteForUser(noteId, email);

		if (note.getIsDeleted()) {
			throw new InvalidNoteStateException("Cannot archive a trashed note");
		}

		note.setIsArchived(!note.getIsArchived());
		return new NoteResponse(noteRepository.save(note));
	}

	public NoteResponse trashNote(int noteId, String email) {
		Note note = getNoteForUser(noteId, email);
		note.setIsDeleted(!note.getIsDeleted());

		if (note.getIsDeleted()) {
			note.setIsPined(false);
			note.setIsArchived(false);
		}

		return new NoteResponse(noteRepository.save(note));
	}

	public void deleteForever(int noteId, String email) {
		Note note = getNoteForUser(noteId, email);
		noteRepository.delete(note);
	}

	public List<NoteResponse> getArchiveNotes(String email) {
		User user = getUser(email);
		return noteRepository.findByOwnerAndIsArchivedTrueAndIsDeletedFalse(user)
				.stream()
				.map(NoteResponse::new)
				.toList();
	}

	public List<NoteResponse> getTrashNotes(String email) {
		User user = getUser(email);
		return noteRepository.findByOwnerAndIsDeletedTrue(user)
				.stream()
				.map(NoteResponse::new)
				.toList();
	}

	private void copyRequestToNote(NoteRequest request, Note note) {
		note.setTitle(request.getTitle());
		note.setDescription(request.getDescription());
		note.setColor(request.getColor());
		note.setTypeOfNote(request.getTypeOfNote());
		note.setImageUrl(request.getImageUrl());
		note.setLinkUrl(request.getLinkUrl());
	}

	private Note getNoteForUser(int noteId, String email) {
		User user = getUser(email);
		return noteRepository.findByNoteIdAndOwner(noteId, user)
				.orElseThrow(() -> new NoteNotFoundException("Note not found"));
	}

	private User getUser(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new NoteNotFoundException("User not found"));
	}
}
