package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.RawData;
import org.example.model.RawDataKey;
import org.example.model.ShortLink;
import org.example.repository.RawDataRepository;
import org.example.repository.ShortLinkRepository;
import org.example.util.exceptions.ResourceDeletedException;
import org.example.util.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShortLinkServiceBean implements ShortLinkService {
    private final ShortLinkRepository repository;
    private final RawDataRepository rawDataRepository;

    @Override
    public ShortLink create(ShortLink link) {
        link.setHash(getHashCode(link));
        return repository.save(link);
    }

    @Override
    public ShortLink getByHash(String hash) {
        ShortLink foundLink = repository.findById(hash).orElseThrow(ResourceNotFoundException::new);
        if (foundLink.isDeleted()) {
            throw new ResourceDeletedException();
        }
        return foundLink;
    }

    @Override
    public ShortLink deleteByHash(String hash) {
        ShortLink toDeleteLink = getByHash(hash);
        toDeleteLink.setDeleted(true);
        repository.save(toDeleteLink);
        return toDeleteLink;
    }

    @Override
    public String getHashCode(ShortLink link) {
        return getHashV3(link);
    }
    @Deprecated
    private String getHashV2(ShortLink link) {
        String hash = String.valueOf(link.getLink().hashCode());
        int length = (int) (Math.random() * 3);
        StringBuilder hashBuilder = new StringBuilder();
        for (int i = 0; i < 3; i++) { //записываем буквы
            if (Math.random() > 0.5) {
                hashBuilder.append((char) ((int) (Math.random() * 25) + 65)); //Случайная буква верхнего регистра
            } else {
                hashBuilder.append((char) ((int) (Math.random() * 25) + 97)); //Случайная буква нижнего регистра
            }
        }
        for (int i = 0; i < length; i++) {//записываем цифры (может быть от 0 до 2 цифр)
            hashBuilder.append(hash.charAt(hash.length() - i - 1));
        }
        return hashBuilder.toString();
    }

    private String getHashV3(ShortLink link) {
        String hash = String.valueOf(Math.abs(link.getLink().hashCode()));
        int length = 3 + (int) (Math.random() * 3);
        StringBuilder hashBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) { //записываем буквы
            double randomDouble = Math.random();
            if (randomDouble > (2F/3F)) {
                hashBuilder.append((char) ((int) (Math.random() * 25) + 65)); //Случайная буква верхнего регистра
            } else if (randomDouble > (1F/3F)) {
                hashBuilder.append((char) ((int) (Math.random() * 25) + 97)); //Случайная буква нижнего регистра
            } else {
                hashBuilder.append(hash.charAt((int) (Math.random() * (hash.length() + 1)))); //берем случайное число из хеша
            }
        }
        return hashBuilder.toString();
    }

    @Override
    public void updateOnStatistics(Duration duration, String hash, boolean isFound) {
        try {
            rawDataRepository.saveData(hash, duration.toMillis(), isFound);
        } catch (Exception ignored) {

        }
    }
}
