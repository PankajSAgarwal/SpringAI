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