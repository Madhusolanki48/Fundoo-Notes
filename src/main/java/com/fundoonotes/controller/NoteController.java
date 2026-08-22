package com.fundoonotes.controller;

import java.security.Principal;
import java.util.List;

import com.fundoonotes.dto.NoteRequest;
import com.fundoonotes.dto.NoteResponse;
import com.fundoonotes.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
public class NoteController {

	private final NoteService noteService;

	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}

	@PostMapping("/addNotes")
	public ResponseEntity<NoteResponse> addNote(@Valid @RequestBody NoteRequest request, Principal principal) {
		NoteResponse response = noteService.addNote(request, principal.getName());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/getNotesList")
	public ResponseEntity<List<NoteResponse>> getNotes(Principal principal) {
		return ResponseEntity.ok(noteService.getNotes(principal.getName()));
	}

	@GetMapping("/getNotesDetail/{noteId}")
	public ResponseEntity<NoteResponse> getNote(@PathVariable int noteId, Principal principal) {
		return ResponseEntity.ok(noteService.getNote(noteId, principal.getName()));
	}

	@PostMapping("/updateNotes")
	public ResponseEntity<NoteResponse> updateNote(@Valid @RequestBody NoteRequest request, Principal principal) {
		return ResponseEntity.ok(noteService.updateNote(request, principal.getName()));
	}

	@PostMapping("/pinUnpinNotes/{noteId}")
	public ResponseEntity<NoteResponse> pinUnpinNote(@PathVariable int noteId, Principal principal) {
		return ResponseEntity.ok(noteService.pinUnpinNote(noteId, principal.getName()));
	}

	@PostMapping("/archiveNotes/{noteId}")
	public ResponseEntity<NoteResponse> archiveNote(@PathVariable int noteId, Principal principal) {
		return ResponseEntity.ok(noteService.archiveNote(noteId, principal.getName()));
	}

	@PostMapping("/trashNotes/{noteId}")
	public ResponseEntity<NoteResponse> trashNote(@PathVariable int noteId, Principal principal) {
		return ResponseEntity.ok(noteService.trashNote(noteId, principal.getName()));
	}

	@PostMapping("/deleteForeverNotes/{noteId}")
	public ResponseEntity<String> deleteForever(@PathVariable int noteId, Principal principal) {
		noteService.deleteForever(noteId, principal.getName());
		return ResponseEntity.ok("Note deleted permanently");
	}

	@GetMapping("/getArchiveNotesList")
	public ResponseEntity<List<NoteResponse>> getArchiveNotes(Principal principal) {
		return ResponseEntity.ok(noteService.getArchiveNotes(principal.getName()));
	}

	@GetMapping("/getTrashNotesList")
	public ResponseEntity<List<NoteResponse>> getTrashNotes(Principal principal) {
		return ResponseEntity.ok(noteService.getTrashNotes(principal.getName()));
	}
}
