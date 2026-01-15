package com.caseclarity.evidence.service;

import com.caseclarity.evidence.domain.EvidenceUploadedEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuditEventPublisher {

    public Mono<Void> publish(EvidenceUploadedEvent event) {
        /*
         * Future:
         * kafkaTemplate.send("evidence-events", event)
         *
         * For now:
         * No-op (fire-and-forget)
         */
        return Mono.empty();
    }
}
