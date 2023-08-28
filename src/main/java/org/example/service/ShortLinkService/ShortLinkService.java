package org.example.service.ShortLinkService;

import org.example.model.ShortLink;

import java.time.Duration;

public interface ShortLinkService {
    ShortLink create(ShortLink link);
    ShortLink getByHash(String hash);
    ShortLink deleteByHash(String hash);
    String getHashCode();
    void updateOnStatistics(Duration duration, String hash, boolean isFound);
    void generateDataRecords(int amount);
}
