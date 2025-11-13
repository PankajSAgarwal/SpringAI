package com.example.boardgamebuddy;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SpringAiBoardGameService implements BoardGameService{

    private final ChatClient chatClient;

    public SpringAiBoardGameService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    private static final String questionPromptTemplate = """
            Answer this question about {gameTitle}: {question}
            """;

    @Override
    public Answer askQuestion(Question question) {
        var answerText = chatClient.prompt()
                .user(
                        userSpec -> userSpec
                                .text(questionPromptTemplate)
                                .param("gameTitle", question.gameTitle())
                                .param("question", question.question()))
                .call()
                .content();
        return new Answer(question.gameTitle(), answerText);
    }
}
