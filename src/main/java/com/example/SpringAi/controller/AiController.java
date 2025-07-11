package com.example.SpringAi.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {
	
	 private ChatClient chatClient;
	 
	 public AiController(OllamaChatModel chatModel) {
		 this.chatClient = ChatClient.create(chatModel);
	 }
	 
	 @GetMapping("/{message}")
	 public ResponseEntity<String> getResponse(@PathVariable String message){
		 
		 String response = chatClient
				 .prompt(message)
				 .call()
				 .content();
		 
		 return ResponseEntity.ok(response);
	 }

}
