package com.github.maratmingazov.config;


import com.github.maratmingazov.tools.StickyNoteToolsService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfiguration {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    public ToolCallbackProvider tools(StickyNoteToolsService stickyNoteToolsService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(stickyNoteToolsService)
                .build();
    }
}
