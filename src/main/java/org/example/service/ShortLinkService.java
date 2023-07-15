package org.example.service;

import org.example.model.ShortLink;

public interface ShortLinkService {
    ShortLink create(ShortLink link);
    ShortLink getById(Long id);
    ShortLink deleteById(Long id);
    ShortLink getByHash(String hash);
    String getHashCode(ShortLink link);
}
