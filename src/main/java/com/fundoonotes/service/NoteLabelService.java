package com.fundoonotes.service;

import java.util.List;

import com.fundoonotes.dto.LabelRequest;
import com.fundoonotes.dto.LabelResponse;
import com.fundoonotes.entity.NoteLabel;
import com.fundoonotes.entity.User;
import com.fundoonotes.exception.LabelAlreadyExistsException;
import com.fundoonotes.exception.LabelNotFoundException;
import com.fundoonotes.repository.NoteLabelRepository;
import com.fundoonotes.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class NoteLabelService {

	private final NoteLabelRepository noteLabelRepository;
	private final UserRepository userRepository;

	public NoteLabelService(NoteLabelRepository noteLabelRepository, UserRepository userRepository) {
		this.noteLabelRepository = noteLabelRepository;
		this.userRepository = userRepository;
	}

	public LabelResponse createLabel(LabelRequest request, String email) {
		User user = getUser(email);
		checkDuplicateLabel(user, request.getLabel());

		NoteLabel noteLabel = new NoteLabel();
		noteLabel.setLabel(request.getLabel());
		noteLabel.setOwner(user);

		return new LabelResponse(noteLabelRepository.save(noteLabel));
	}

	public LabelResponse updateLabel(int id, LabelRequest request, String email) {
		User user = getUser(email);
		NoteLabel noteLabel = getLabelForUser(id, user);

		if (!noteLabel.getLabel().equalsIgnoreCase(request.getLabel())) {
			checkDuplicateLabel(user, request.getLabel());
		}

		noteLabel.setLabel(request.getLabel());
		return new LabelResponse(noteLabelRepository.save(noteLabel));
	}

	public void deleteLabel(int id, String email) {
		User user = getUser(email);
		NoteLabel noteLabel = getLabelForUser(id, user);
		noteLabel.setIsDeleted(true);
		noteLabelRepository.save(noteLabel);
	}

	public List<LabelResponse> getLabels(String email) {
		User user = getUser(email);
		return noteLabelRepository.findByOwnerAndIsDeletedFalse(user)
				.stream()
				.map(LabelResponse::new)
				.toList();
	}

	private void checkDuplicateLabel(User user, String label) {
		noteLabelRepository.findByOwnerAndLabelIgnoreCaseAndIsDeletedFalse(user, label)
				.ifPresent(existingLabel -> {
					throw new LabelAlreadyExistsException("Label already exists");
				});
	}

	private NoteLabel getLabelForUser(int id, User user) {
		return noteLabelRepository.findByIdAndOwner(id, user)
				.orElseThrow(() -> new LabelNotFoundException("Label not found"));
	}

	private User getUser(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new LabelNotFoundException("User not found"));
	}
}
