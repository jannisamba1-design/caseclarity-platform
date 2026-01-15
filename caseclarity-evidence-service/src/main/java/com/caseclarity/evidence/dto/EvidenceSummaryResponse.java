package com.caseclarity.evidence.dto;

import com.caseclarity.evidence.domain.Evidence;

import java.time.Instant;
import java.util.UUID;

public record EvidenceSummaryResponse(
        UUID evidenceId,
        String hash,
        UUID uploadedBy,
        Instant uploadedAt
) {
    public static EvidenceSummaryResponse from(Evidence e) {
        return new EvidenceSummaryResponse(
                e.getEvidenceId(),
                e.getContentHash(),
                e.getUploadedBy(),
                e.getUploadedAt()
        );
    }
}
