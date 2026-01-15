package com.caseclarity.evidence.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.core.io.buffer.DataBufferUtils;
import reactor.core.publisher.Mono;

@Service
public class HashService {

    public Mono<String> computeHash(FilePart file) {
        return DataBufferUtils.join(file.content())
                .map(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);
                    return DigestUtils.sha256Hex(bytes);
                });
    }
}
