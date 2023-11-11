package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.DataEntity;
import org.example.model.ShortLink;
import org.example.model.User;
import org.example.repository.DataRepository;
import org.example.repository.ShortLinkRepository;
import org.example.util.exceptions.ResourceDeletedException;
import org.example.util.exceptions.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ShortLinkService {
    private final ShortLinkRepository repository;
    private final DataRepository rawDataRepository;

    public ShortLink create(ShortLink link) {
        link.setHash(getHashCode());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        link.setUser(userDetails);
        return repository.save(link);
    }

    public ShortLink getByHash(String hash) {
        ShortLink foundLink = repository.findById(hash).orElseThrow(ResourceNotFoundException::new);
        if (foundLink.isDeleted()) {
            throw new ResourceDeletedException();
        }
        return foundLink;
    }

    public ShortLink deleteByHash(String hash) {
        ShortLink toDeleteLink = getByHash(hash);
        toDeleteLink.setDeleted(true);
        repository.save(toDeleteLink);
        return toDeleteLink;
    }

    public String getHashCode() {
        Random random = new Random();
        String result;
        StringBuilder hashBuilder;
        int counter = 0;
        int length = 3;
        do {
            hashBuilder = new StringBuilder();
            for (int i = 0; i < length; i++) { //записываем буквы
                int symbol = random.nextInt(62);
                if (symbol < 26) {
                    hashBuilder.append((char) ('a' + symbol));
                } else if (symbol < 52) {
                    hashBuilder.append((char) ('A' + symbol - 26));
                } else {
                    hashBuilder.append((char) ('0' + (symbol - 52)));
                }
            }
            result = hashBuilder.toString();
            counter++;
            if (counter % 10 == 0) { //Каждые 10 раз увеличиваем длину
                length++;
            }
            if (counter == 30) {
                throw new RuntimeException("hash was not able not be formed");
            }
        } while (repository.checkIfUniqueHash(result) != 0);
        return result;
    }

    public void updateOnStatistics(Duration duration, String hash, User user, boolean isFound) {
        DataEntity newRecord = DataEntity.builder()
                .time(Instant.now().atOffset(ZoneOffset.UTC).toInstant())
                .hash(hash)
                .lag(duration.toMillis())
                .isFound(isFound)
                .user(user)
                .build();

        List<DataEntity> lastTwoRecords = rawDataRepository.findLast(2, user.getId());
        int size = lastTwoRecords.size();

        if (size > 0) {
            DataEntity lastRecord = size == 2 ? lastTwoRecords.get(1) : lastTwoRecords.get(0); //из-за сортировки они будут меняться местами
            DataEntity preLastRecord = size == 2 ? lastTwoRecords.get(0) : null;

            long calculatedDuration;
            if (preLastRecord != null && lastRecord.getHash().equals(preLastRecord.getHash())) {
                calculatedDuration = Duration.between(lastRecord.getTime().minusMillis(preLastRecord.getExpectedDuration()), newRecord.getTime()).toMillis();
            } else {
                calculatedDuration = Duration.between(lastRecord.getTime(), newRecord.getTime()).toMillis();
            }
            lastRecord.setExpectedDuration(calculatedDuration);
            rawDataRepository.save(lastRecord);
        }
        rawDataRepository.save(newRecord);
    }
}
