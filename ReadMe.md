## Run the program
1. SET the environment variable in IDE and CLI
```shell
export SPRING_AI_OPENAI_API_KEY
```
2. Run the project
```shell
mvn spring-boot:run
```
3. Send a prompt to endpoint /ask using HTTPie
```shell
   http :8080/ask question="What is the capital of India?" -b
```
***Note:*** -b flag indicates that you only want the body of the request printed. 
If you omit it the request headers will also be displayed. 

4. To see OpenAI models , https://platform.openai.com/docs/models
5. To specify an openai model to use other that provided by spring default
```properties
spring.ai.openai.chat.options.model=gpt-4.1-nano
```

## Serving models locally with ollama
1. Pulling models(e.g gemma:2b) to local machine using `ollama`
```shell
ollama pull gemma:2b
```
```shell
ollama pull mistral:7b
```
2. ollama official library
https://ollama.com/library
3. to see a list of models installed on your machine
```shell
ollama list
```
4.More details on the models installed , send a `GET` request to ollama's API `/api/tags` endpoint

```shell
http http://localhost:11434/api/tags -b
```
5. spring ai ollama starter dependency
6. No api keys required to use model from ollama
7. Spring AI defaults to the Mistral 7B when using ollama
8. If you need to use a different model
```properties
spring.ai.ollama.chat.model=gemma:2b
```
9. pull model strategy for ollama models in spring-ai. Default value is `never`. Other values are `when_missing` or `always`
```properties
spring.ai.ollama.init.pull-model-strategy=when_missing
```

## Ensuring relevant answers

- Spring AI's `RelevancyEvaluator`
Determine whether an answer is relevant to the given question 
- Spring AI's `FactCheckingEvaluator`
Instead of asking LLM to judge the relevancy of the answer to the question, it asks the LLM to judge whether the answer correctly answers the questions.
## PromptTemplates
- To test prompt template use the below command using HTTPie
```shell
http :8080/ask gameTitle="checkers" question="How many pieces are there?" -b
```

## To see raw request and response sent and received from LLM
- Add the below dependency
```xml
<dependency>
    <groupId>org.zalando</groupId>
    <artifactId>logbook-spring-boot-starter</artifactId>
    <version>3.9.0</version>
</dependency>
```
- Spring AI ChatClient uses Spring's `RestClient` under the covers, so declare a `RestClientCustomizer` bean to add Logbook's `LogbookClientHttpRequestInterceptor` as a request interceptor  
```java
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;
@Bean
RestClientCustomizer logBookCustomizer(LogbookClientHttpRequestInterceptor interceptor) {
    return restClient -> restClient.requestInterceptor(interceptor);
}
```
- Logbook logs request and response details at `TRACE` level,so set the logging level in the `application.properties` file to emit logging at this level.
```properties
logging.level.org.zalando.logbook = TRACE
```
- By default, Logbook logs the requests and responses in `JSON` format that can make it difficult to read request and responses. So optionally set `logbook.format.style=http`.

```properties
logbook.format.style=http
```
## Adjusting variability
Applying options like temperature, Top-P and Top-K are helpful in gaining some control over the choices made when LLM is generating a response
### Setting the temperature globally in application.properties
- OpenAI
```properties
spring.ai.openai.chat.options.temperature=0.7
```
- ollama
```properties
spring.ai.ollama.chat.options.temperature=0.7
```

### Setting temperature programmatically

```java
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;

ChatOptions chatOptions = ChatOptions.builder()
        .temperature(0.7)
        .build();

ChatClient chatClient = ChatClient.builder()
        .defaultOptions(chatOptions)
        .build();
```
### Setting top-p in application.properties

```properties
spring.ai.openai.chat.options.top-p=0.8
```
- ollama
```properties
spring.ai.ollama.chat.options.top-p=0.8
```
### Setting top-p programmatically

```java
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;

ChatOptions chatOptions = ChatOptions.builder()
        .topP(0.7)
        .build();

ChatClient chatClient = ChatClient.builder()
        .defaultOptions(chatOptions)
        .build();
```
### Setting top-k in application.properties

- OpenAI does not support specifying Top-K when submitting a prompt
- ollama
```properties
spring.ai.ollama.chat.options.top-k=4
```

