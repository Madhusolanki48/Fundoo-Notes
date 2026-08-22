package com.fundoonotes.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public class ReminderRequest {

	private int noteId;

	@NotEmpty(message = "At least one reminder is required")
	private List<String> reminder = new ArrayList<>();

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public List<String> getReminder() {
		return reminder;
	}

	public void setReminder(List<String> reminder) {
		this.reminder = reminder;
	}
}
