package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.DataEntity;
import org.example.repository.DataRepository;
import org.example.util.exceptions.HashNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {
    private final DataRepository repository;

    public List<DataEntity> getAll() {
        List<DataEntity> result = repository.findAll();
        return result.isEmpty() ? result : formatLastRecord(result);
    }

    public List<DataEntity> getAllWithHash(String hash) {
        List<DataEntity> result = repository.findAllByHash(hash);
        if (result.isEmpty()) {
            throw new HashNotFoundException();
        }

        return formatLastRecord(result);
    }

    public List<DataEntity> getAll(int amount) {
        if (amount < 2) {
            throw new IllegalArgumentException("Amount must be bigger than 1");
        }
        List<DataEntity> result = repository.findLast(amount);
        return result.isEmpty() ? result : formatLastRecord(result);
    }

    public List<DataEntity> getAllWithHash(String hash, int amount) {
        if (amount < 2) {
            throw new IllegalArgumentException("Amount must be bigger than 1");
        }
        List<DataEntity> resultList = repository.findLastByHash(hash, amount);
        if (resultList.isEmpty()) {
            throw new HashNotFoundException();
        }
        return formatLastRecord(resultList);
    }

    private static List<DataEntity> formatLastRecord(List<DataEntity> result) { //Более оптимизированая версия, не использует циклы. Интересно у Вас узнать, какая лучше
        int size = result.size();

        if (size > 0) {
            DataEntity lastRecord = result.get(result.size() - 1);
            DataEntity preLastRecord = size > 2 ? result.get(result.size() - 2) : null;

            long calculatedDuration;
            if (preLastRecord != null && lastRecord.getHash().equals(preLastRecord.getHash())) {
                calculatedDuration = Duration.between(lastRecord.getTime().minusMillis(preLastRecord.getExpectedDuration()), Instant.now().atOffset(ZoneOffset.UTC)).toMillis();
            } else {
                calculatedDuration = Duration.between(lastRecord.getTime(), Instant.now().atOffset(ZoneOffset.UTC)).toMillis();
            }
            lastRecord.setExpectedDuration(calculatedDuration);
        }

        return result;
    }
}
