package com.fundoonotes.repository;

import java.util.ArrayList;
import java.util.List;

import com.fundoonotes.entity.Note;
import com.fundoonotes.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class NoteSpecification {

	public static Specification<Note> search(User owner, String titleText, String state, String labelName) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(root.get("owner"), owner));

			if (titleText != null && !titleText.isBlank()) {
				String text = "%" + titleText.toLowerCase() + "%";
				Predicate titleMatch = cb.like(cb.lower(root.get("title")), text);
				Predicate descriptionMatch = cb.like(cb.lower(root.get("description")), text);
				predicates.add(cb.or(titleMatch, descriptionMatch));
			}

			if (state != null && !state.isBlank()) {
				String noteState = state.toUpperCase();

				if (noteState.equals("PINNED")) {
					predicates.add(cb.equal(root.get("isPined"), true));
					predicates.add(cb.equal(root.get("isDeleted"), false));
				} else if (noteState.equals("ARCHIVED")) {
					predicates.add(cb.equal(root.get("isArchived"), true));
					predicates.add(cb.equal(root.get("isDeleted"), false));
				} else if (noteState.equals("TRASH")) {
					predicates.add(cb.equal(root.get("isDeleted"), true));
				} else {
					predicates.add(cb.equal(root.get("isDeleted"), false));
				}
			} else {
				predicates.add(cb.equal(root.get("isDeleted"), false));
			}

			if (labelName != null && !labelName.isBlank()) {
				predicates.add(cb.equal(cb.lower(root.join("labels").get("label")), labelName.toLowerCase()));
			}

			query.distinct(true);
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
