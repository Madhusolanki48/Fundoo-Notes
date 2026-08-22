package com.fundoonotes.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

@Service
public class ReminderMessageService {

	private final ExecutorService executorService = Executors.newSingleThreadExecutor();

	public void sendReminderMessage(int noteId, List<String> reminders) {
		executorService.submit(() -> {
			System.out.println("Reminder message received for note id: " + noteId);
			System.out.println("Reminder values: " + reminders);
		});
	}
}
