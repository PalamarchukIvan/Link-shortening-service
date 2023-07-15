package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.ShortLink;
import org.example.repository.ShortLinkRepository;
import org.example.util.exceptions.ResourceDeletedException;
import org.example.util.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShortLinkServiceBean implements  ShortLinkService {
    private final ShortLinkRepository repository;

    @Override
    public ShortLink create(ShortLink link) {
        if(!link.getLink().contains("http")) {
            link.setLink("https://".concat(link.getLink()));
        }
        link.setHash(getHashCode(link));
        return repository.save(link);
    }

    @Override
    public ShortLink getById(Long id) {
        ShortLink foundLink = repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        if(foundLink.isDeleted()) {
            throw new ResourceDeletedException();
        }
        return foundLink;
    }

    @Override
    public ShortLink deleteById(Long id) {
        ShortLink toDeleteLink = getById(id);
        toDeleteLink.setDeleted(true);
        repository.save(toDeleteLink);
        return toDeleteLink;
    }

    @Override
    public ShortLink getByHash(String hash) {
        return repository.findByHash(hash).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public String getHashCode(ShortLink link) {
        return String.valueOf(link.getLink().hashCode());
    }
}
