package com.fundoonotes.controller;

import java.security.Principal;
import java.util.List;

import com.fundoonotes.dto.LabelRequest;
import com.fundoonotes.dto.LabelResponse;
import com.fundoonotes.service.NoteLabelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/noteLabels")
public class NoteLabelController {

	private final NoteLabelService noteLabelService;

	public NoteLabelController(NoteLabelService noteLabelService) {
		this.noteLabelService = noteLabelService;
	}

	@PostMapping
	public ResponseEntity<LabelResponse> createLabel(@Valid @RequestBody LabelRequest request, Principal principal) {
		LabelResponse response = noteLabelService.createLabel(request, principal.getName());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<LabelResponse> updateLabel(@PathVariable int id,
			@Valid @RequestBody LabelRequest request, Principal principal) {
		return ResponseEntity.ok(noteLabelService.updateLabel(id, request, principal.getName()));
	}

	@DeleteMapping("/{id}/deleteNoteLabel")
	public ResponseEntity<String> deleteLabel(@PathVariable int id, Principal principal) {
		noteLabelService.deleteLabel(id, principal.getName());
		return ResponseEntity.ok("Label deleted successfully");
	}

	@GetMapping("/getNoteLabelList")
	public ResponseEntity<List<LabelResponse>> getLabels(Principal principal) {
		return ResponseEntity.ok(noteLabelService.getLabels(principal.getName()));
	}
}
