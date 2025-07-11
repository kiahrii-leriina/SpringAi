package com.example.SpringAi.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;

@RestController
public class StreamingAiController {

    private final ChatClient chatClient;

    public StreamingAiController(OllamaChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    @GetMapping(path = "/stream/{message}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamResponse(@PathVariable String message) {
        SseEmitter emitter = new SseEmitter();

        new Thread(() -> {
            try {
                ((Object) chatClient
                    .prompt(message)
                    .stream())
                    .map(chunk -> chunk.content())
                    .doOnError(emitter::completeWithError)
                    .doOnComplete(emitter::complete)
                    .subscribe(
                        content -> {
                            try {
                                emitter.send(content);
                                Thread.sleep(Duration.ofMillis(50).toMillis());
                            } catch (IOException | InterruptedException e) {
                                emitter.completeWithError(e);
                            }
                        }
                    );

            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        }).start();

        return emitter;
    }
}
