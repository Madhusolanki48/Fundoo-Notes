package com.fundoonotes.dto;

import jakarta.validation.constraints.NotBlank;

public class NoteRequest {

	private int noteId;

	@NotBlank(message = "Title is required")
	private String title;

	private String description;
	private String color;
	private String typeOfNote;
	private String imageUrl;
	private String linkUrl;

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTypeOfNote() {
		return typeOfNote;
	}

	public void setTypeOfNote(String typeOfNote) {
		this.typeOfNote = typeOfNote;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
}
