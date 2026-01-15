package com.caseclarity.evidence.service;

import com.caseclarity.evidence.domain.Evidence;
import com.caseclarity.evidence.domain.EvidenceUploadedEvent;
import com.caseclarity.evidence.dto.EvidenceSummaryResponse;
import com.caseclarity.evidence.dto.EvidenceUploadResponse;
import com.caseclarity.evidence.port.EvidenceStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvidenceService {

    private final EvidenceStore evidenceStore;
    private final HashService hashService;
    private final StorageService storageService;
    private final AuditEventPublisher eventPublisher;

    public Mono<EvidenceUploadResponse> upload(
            UUID tenantId,
            UUID userId,
            String scopes,
            UUID caseId,
            FilePart file
    ) {
        if (!scopes.contains("evidence:write")) {
            return Mono.error(new AccessDeniedException("Missing scope"));
        }

        UUID evidenceId = UUID.randomUUID();
        Instant now = Instant.now();

        return hashService.computeHash(file)
                .flatMap(hash ->
                        evidenceStore.findLatestHash(tenantId, caseId)
                                .defaultIfEmpty("GENESIS")
                                .flatMap(prevHash ->
                                        storageService.store(
                                                tenantId, caseId, evidenceId, file
                                        ).then(
                                                evidenceStore.save(
                                                        Evidence.builder()
                                                                .tenantId(tenantId)
                                                                .caseId(caseId)
                                                                .evidenceId(evidenceId)
                                                                .contentHash(hash)
                                                                .previousHash(prevHash)
                                                                .uploadedBy(userId)
                                                                .uploadedAt(now)
                                                                .build()
                                                )
                                        ).then(
                                                eventPublisher.publish(
                                                        EvidenceUploadedEvent.builder()
                                                                .tenantId(tenantId)
                                                                .caseId(caseId)
                                                                .evidenceId(evidenceId)
                                                                .hash(hash)
                                                                .uploadedBy(userId)
                                                                .timestamp(now)
                                                                .build()
                                                )
                                        ).thenReturn(
                                                new EvidenceUploadResponse(
                                                        evidenceId, caseId, hash
                                                )
                                        )
                                )
                );
    }

    public Flux<EvidenceSummaryResponse> listByCase(
            UUID tenantId,
            UUID caseId
    ) {
        return evidenceStore
                .findByTenantAndCase(tenantId, caseId)
                .map(EvidenceSummaryResponse::from);
    }
}
