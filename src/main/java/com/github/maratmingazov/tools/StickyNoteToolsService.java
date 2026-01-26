package com.github.maratmingazov.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class StickyNoteToolsService {

    private final RestClient restClient;


    @Tool(name = "create StickyNote", description = "create a sticky note widget (sticker) on a miro board")
    public Response createStickyNote(
            @ToolParam(description = "Miro board key for a sticker creation", required = true) String boardKey,
            @ToolParam(description = "The text to be displayed on the sticker", required = true) String text,
            @ToolParam(description = "Sticker position x coordinate") Integer x,
            @ToolParam(description = "Sticker position y coordinate") Integer y
    ) {
        var request = new Request(boardKey, text, x, y);
        return restClient.post()
                .uri("/stickyNote")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept", "application/json")
                .body(request)
                .retrieve()
                .body(Response.class);
    }

    @Tool(name = "delete StickyNote", description = "delete a sticky note widget (sticker) on a miro board")
    public void deleteStickyNote(
            @ToolParam(description = "Miro board key for a sticker creation", required = true) String boardKey,
            @ToolParam(description = "StickyNote id for deletion", required = true) Long stickyNoteId
    ) {
        restClient.delete()
                .uri("/stickyNote?boardKey={boardKey}&widgetId={stickyNoteId}", boardKey, stickyNoteId)
                .retrieve()
                .toBodilessEntity();
    }


    record Request(String boardKey, String text, Integer x, Integer y) {}
    record Response(Long id, String text, String color, Integer x, Integer y) {}
}

