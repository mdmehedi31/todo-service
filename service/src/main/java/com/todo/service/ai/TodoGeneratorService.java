package com.todo.service.ai;


import com.todom.library.dto.TodoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TodoGeneratorService implements ITodoGeneratorService {


    private final ChatClient chatClient;
    private static final Logger log = LoggerFactory.getLogger(TodoGeneratorService.class);

    public TodoGeneratorService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @Override
    public TodoDTO generateTodoByTopics(String message) {

        try{

            log.debug("message: {}", message);
            String systemMessageValue="List should follow learning step.";

            String promptValue = "Create a todo list on this topics {message} " +
                    "{format}";
            BeanOutputConverter<TodoDTO> converter = new BeanOutputConverter<>(new ParameterizedTypeReference<TodoDTO>(){});
            PromptTemplate template = new PromptTemplate(promptValue);
            Prompt prompt = template.create(Map.of("message",message, "format",converter.getFormat()));

            Generation result =  chatClient.prompt(prompt).system(systemMessageValue).call().chatResponse().getResult();

          return  converter.convert(result.getOutput().getText());

        }catch (Exception e) {

            log.error("An exception thrown when generate todo ", e);
            return new TodoDTO();
        }
    }
}
