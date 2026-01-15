package com.caseclarity.evidence.controller;

import com.caseclarity.evidence.dto.EvidenceSummaryResponse;
import com.caseclarity.evidence.dto.EvidenceUploadResponse;
import com.caseclarity.evidence.service.EvidenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/evidence")
@Slf4j
@RequiredArgsConstructor
public class EvidenceController {

    private final EvidenceService evidenceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<EvidenceUploadResponse>> uploadEvidence(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-Scopes") String scopes,
            @RequestPart("file") FilePart file,
            @RequestPart("caseId") String caseId
    ) {
        log.debug("===== Inside uploadEvidence =====");
        UUID caseUuid = UUID.fromString(caseId);
        log.info("caseId {}, tenantId {}, userId {}, scopes{}", caseUuid, tenantId, userId, scopes);
        return evidenceService.upload(tenantId, userId, scopes, caseUuid, file)
                .map(resp ->
                        ResponseEntity.status(HttpStatus.CREATED).body(resp)
                );
    }

    @GetMapping
    public Flux<EvidenceSummaryResponse> listEvidence(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @RequestParam UUID caseId
    ) {
        log.info("===== Inside listEvidence =====");
        log.info("caseId {}, tenantId {}", caseId, tenantId);
        return evidenceService.listByCase(tenantId, caseId);
    }
}
