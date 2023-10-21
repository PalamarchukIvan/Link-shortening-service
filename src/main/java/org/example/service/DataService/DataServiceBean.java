package org.example.service.DataService;

import lombok.RequiredArgsConstructor;
import org.example.model.Data;
import org.example.repository.RawDataRepository;
import org.example.util.exceptions.HashNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataServiceBean implements DataService {
    private final RawDataRepository repository;

    @Override
    public List<Data> getAll() {
        List<Data> result = repository.findAll();
        return result.isEmpty() ? result : formatLastRecord(result);
    }

    @Override
    public List<Data> getAllWithHash(String hash) {
        List<Data> result = repository.findAllByHash(hash);
        if (result.isEmpty()) {
            throw new HashNotFoundException();
        }

        return formatLastRecord(result);
    }

    @Override
    public List<Data> getAll(int amount) {
        if (amount < 2) {
            throw new IllegalArgumentException("Amount must be bigger than 1");
        }
        List<Data> result = repository.findLast(amount);
        return result.isEmpty() ? result : formatLastRecord(result);
    }

    @Override
    public List<Data> getAllWithHash(String hash, int amount) {
        if (amount < 2) {
            throw new IllegalArgumentException("Amount must be bigger than 1");
        }
        List<Data> resultList = repository.findLastByHash(hash, amount);
        if (resultList.isEmpty()) {
            throw new HashNotFoundException();
        }
        return formatLastRecord(resultList);
    }

    private static List<Data> formatLastRecord(List<Data> result) { //Более оптимизированая версия, не использует циклы. Интересно у Вас узнать, какая лучше
        int size = result.size();

        if(size > 0) {
            Data lastRecord = result.get(result.size() - 1);
            Data preLastRecord = size > 2 ? result.get(result.size() - 2) : null;

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
