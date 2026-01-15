package com.caseclarity.evidence.dto;

import java.util.UUID;

public record EvidenceUploadResponse(
        UUID evidenceId,
        UUID caseId,
        String hash
) {}
