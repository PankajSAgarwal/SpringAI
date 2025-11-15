package com.example.boardgamebuddy;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class SpringAiBoardGameService implements BoardGameService{

    private final ChatClient chatClient;
    private final GameRulesService gameRulesService;

    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    Resource promptTemplate;
    public SpringAiBoardGameService(ChatClient.Builder chatClientBuilder, GameRulesService gameRulesService) {
        this.chatClient = chatClientBuilder.build();
        this.gameRulesService = gameRulesService;
    }

    /*
    private static final String questionPromptTemplate = """
            Answer this question about {gameTitle}: {question}
            """;
     */

    @Override
    public Answer askQuestion(Question question) {
        var gameRules = gameRulesService.getRulesFor(question.gameTitle());
        var answerText = chatClient.prompt()
                .system(
                        systemSpec -> systemSpec
                                .text(promptTemplate)
                                .param("gameTitle", question.gameTitle())
                                .param("rules", gameRules))
                .user(question.question())
                .call()
                .content();
        return new Answer(question.gameTitle(), answerText);
    }
}
