package com.fundoonotes.repository;

import java.util.List;
import java.util.Optional;

import com.fundoonotes.entity.NoteLabel;
import com.fundoonotes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteLabelRepository extends JpaRepository<NoteLabel, Integer> {

	List<NoteLabel> findByOwnerAndIsDeletedFalse(User owner);

	Optional<NoteLabel> findByIdAndOwner(int id, User owner);

	Optional<NoteLabel> findByOwnerAndLabelIgnoreCase(User owner, String label);

	Optional<NoteLabel> findByOwnerAndLabelIgnoreCaseAndIsDeletedFalse(User owner, String label);
}
