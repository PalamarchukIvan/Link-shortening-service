package org.example.service;

import org.example.model.RawData;
import org.example.model.ShortLink;

import java.time.Duration;
import java.time.Instant;

public interface ShortLinkService {
    ShortLink create(ShortLink link);
    ShortLink getById(Long id);
    ShortLink deleteById(Long id);
    ShortLink getByHash(String hash);
    String getHashCode(ShortLink link);
    void updateOnStatistics(Duration duration, String hash, boolean isFound);
}
