package com.fundoonotes.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notes")
public class Note {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int noteId;

	private String title;
	private String description;
	private boolean isPined = false;
	private boolean isArchived = false;
	private boolean isDeleted = false;
	private String color;
	private String typeOfNote;
	private String imageUrl;
	private String linkUrl;

	@ElementCollection
	@CollectionTable(name = "note_reminder_list", joinColumns = @JoinColumn(name = "note_id"))
	@Column(name = "reminder")
	private List<String> reminders = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User owner;

	@ManyToMany
	@JoinTable(name = "note_note_labels",
			joinColumns = @JoinColumn(name = "note_id"),
			inverseJoinColumns = @JoinColumn(name = "label_id"))
	private Set<NoteLabel> labels = new HashSet<>();

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

	public boolean getIsPined() {
		return isPined;
	}

	public void setIsPined(boolean isPined) {
		this.isPined = isPined;
	}

	public boolean getIsArchived() {
		return isArchived;
	}

	public void setIsArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
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

	public List<String> getReminders() {
		return reminders;
	}

	public void setReminders(List<String> reminders) {
		this.reminders = reminders;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<NoteLabel> getLabels() {
		return labels;
	}

	public void setLabels(Set<NoteLabel> labels) {
		this.labels = labels;
	}
}
