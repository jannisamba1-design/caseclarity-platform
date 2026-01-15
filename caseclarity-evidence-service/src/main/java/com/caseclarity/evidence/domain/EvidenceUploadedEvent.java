package com.caseclarity.evidence.domain;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record EvidenceUploadedEvent(
        UUID tenantId,
        UUID caseId,
        UUID evidenceId,
        String hash,
        UUID uploadedBy,
        Instant timestamp
) {}
