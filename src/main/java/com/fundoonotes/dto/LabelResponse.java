package com.fundoonotes.dto;

import com.fundoonotes.entity.NoteLabel;

public class LabelResponse {

	private int id;
	private String label;
	private boolean isDeleted;

	public LabelResponse(NoteLabel noteLabel) {
		this.id = noteLabel.getId();
		this.label = noteLabel.getLabel();
		this.isDeleted = noteLabel.getIsDeleted();
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}
}
