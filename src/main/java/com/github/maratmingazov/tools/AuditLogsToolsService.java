package com.github.maratmingazov.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class AuditLogsToolsService {

    private final RestClient restClient;

    public AuditLogsToolsService(@Qualifier("auditLogsRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Tool(name = "getAuditLogs", description = "retrieve audit logs for a miro board with pagination")
    public AuditLogsResponse getAuditLogs(
            @ToolParam(description = "Miro board key for audit logs retrieval", required = true) String boardKey,
            @ToolParam(description = "Offset for pagination (starting from 1)", required = false) Integer offset,
            @ToolParam(description = "Limit of records to retrieve (default 20)", required = false) Integer limit
    ) {
        int actualOffset = offset != null ? offset : 1;
        int actualLimit = limit != null ? limit : 20;

        return restClient.get()
                .uri("/audit-logs?offset={offset}&limit={limit}&boardKey={boardKey}", actualOffset, actualLimit, boardKey)
                .retrieve()
                .body(AuditLogsResponse.class);
    }

    record AuditLog(String eventType, String boardKey, Long widgetId, String widgetType, String occuredAt) {}
    record Paging(Integer offset, Integer limit, Integer count, Integer total) {}
    record AuditLogsResponse(List<AuditLog> data, Paging paging) {}
}
