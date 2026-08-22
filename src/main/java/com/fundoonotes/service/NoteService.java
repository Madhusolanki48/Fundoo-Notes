package com.fundoonotes.service;

import java.util.List;

import com.fundoonotes.dto.NoteRequest;
import com.fundoonotes.dto.NoteResponse;
import com.fundoonotes.dto.ReminderRequest;
import com.fundoonotes.entity.Note;
import com.fundoonotes.entity.NoteLabel;
import com.fundoonotes.entity.User;
import com.fundoonotes.exception.InvalidNoteStateException;
import com.fundoonotes.exception.LabelNotFoundException;
import com.fundoonotes.exception.NoteNotFoundException;
import com.fundoonotes.repository.NoteLabelRepository;
import com.fundoonotes.repository.NoteRepository;
import com.fundoonotes.repository.NoteSpecification;
import com.fundoonotes.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

	private final NoteRepository noteRepository;
	private final UserRepository userRepository;
	private final NoteLabelRepository noteLabelRepository;
	private final ReminderMessageService reminderMessageService;

	public NoteService(NoteRepository noteRepository, UserRepository userRepository,
			NoteLabelRepository noteLabelRepository, ReminderMessageService reminderMessageService) {
		this.noteRepository = noteRepository;
		this.userRepository = userRepository;
		this.noteLabelRepository = noteLabelRepository;
		this.reminderMessageService = reminderMessageService;
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

	public NoteResponse addLabelToNote(int noteId, int labelId, String email) {
		User user = getUser(email);
		Note note = getNoteForUser(noteId, email);
		NoteLabel label = getLabelForUser(labelId, user);

		if (label.getIsDeleted()) {
			throw new LabelNotFoundException("Label not found");
		}

		note.getLabels().add(label);
		return new NoteResponse(noteRepository.save(note));
	}

	public NoteResponse removeLabelFromNote(int noteId, int labelId, String email) {
		User user = getUser(email);
		Note note = getNoteForUser(noteId, email);
		NoteLabel label = getLabelForUser(labelId, user);

		note.getLabels().remove(label);
		return new NoteResponse(noteRepository.save(note));
	}

	public List<NoteResponse> getNotesByLabel(String labelName, String email) {
		User user = getUser(email);
		return noteRepository.findByOwnerAndLabelsLabelIgnoreCaseAndIsDeletedFalse(user, labelName)
				.stream()
				.map(NoteResponse::new)
				.toList();
	}

	public List<NoteResponse> searchNotes(String titleText, String state, String labelName, String email) {
		User user = getUser(email);
		return noteRepository.findAll(NoteSpecification.search(user, titleText, state, labelName))
				.stream()
				.map(NoteResponse::new)
				.toList();
	}

	public NoteResponse addUpdateReminder(ReminderRequest request, String email) {
		Note note = getNoteForUser(request.getNoteId(), email);

		if (note.getIsDeleted()) {
			throw new InvalidNoteStateException("Cannot add reminder to a trashed note");
		}

		note.setReminders(request.getReminder());
		Note savedNote = noteRepository.save(note);
		reminderMessageService.sendReminderMessage(savedNote.getNoteId(), savedNote.getReminders());

		return new NoteResponse(savedNote);
	}

	public NoteResponse removeReminder(ReminderRequest request, String email) {
		Note note = getNoteForUser(request.getNoteId(), email);
		note.getReminders().removeAll(request.getReminder());
		return new NoteResponse(noteRepository.save(note));
	}

	public List<NoteResponse> getReminderNotes(String email) {
		User user = getUser(email);
		return noteRepository.findByOwnerAndIsDeletedFalseAndRemindersIsNotEmpty(user)
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

	private NoteLabel getLabelForUser(int labelId, User user) {
		return noteLabelRepository.findByIdAndOwner(labelId, user)
				.orElseThrow(() -> new LabelNotFoundException("Label not found"));
	}
}
