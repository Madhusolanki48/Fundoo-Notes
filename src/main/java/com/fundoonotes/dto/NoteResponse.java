package com.fundoonotes.dto;

import java.util.List;

import com.fundoonotes.entity.Note;
import com.fundoonotes.entity.NoteLabel;

public class NoteResponse {

	private int noteId;
	private String title;
	private String description;
	private boolean isPined;
	private boolean isArchived;
	private boolean isDeleted;
	private String color;
	private String typeOfNote;
	private String imageUrl;
	private String linkUrl;
	private List<String> labels;
	private List<String> reminders;

	public NoteResponse(Note note) {
		this.noteId = note.getNoteId();
		this.title = note.getTitle();
		this.description = note.getDescription();
		this.isPined = note.getIsPined();
		this.isArchived = note.getIsArchived();
		this.isDeleted = note.getIsDeleted();
		this.color = note.getColor();
		this.typeOfNote = note.getTypeOfNote();
		this.imageUrl = note.getImageUrl();
		this.linkUrl = note.getLinkUrl();
		this.reminders = note.getReminders();
		this.labels = note.getLabels().stream()
				.filter(label -> !label.getIsDeleted())
				.map(NoteLabel::getLabel)
				.toList();
	}

	public int getNoteId() {
		return noteId;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public boolean getIsPined() {
		return isPined;
	}

	public boolean getIsArchived() {
		return isArchived;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public String getColor() {
		return color;
	}

	public String getTypeOfNote() {
		return typeOfNote;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public List<String> getLabels() {
		return labels;
	}

	public List<String> getReminders() {
		return reminders;
	}
}
