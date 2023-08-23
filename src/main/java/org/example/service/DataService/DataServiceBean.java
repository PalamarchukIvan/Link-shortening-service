package org.example.service.DataService;

import lombok.RequiredArgsConstructor;
import org.example.model.AnalyzedData;
import org.example.repository.DataRepository;
import org.example.util.exceptions.HashNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataServiceBean implements DataService {
    private final DataRepository repository;

    @Override
    public List<AnalyzedData> getAll() {
        List<AnalyzedData> result = repository.findAllRaws();
        return result.isEmpty() ? result : formatLastRecord(result);
    }

    @Override
    public List<AnalyzedData> getAllWithHash(String hash) {
        List<AnalyzedData> result = repository.findAllByHash(hash);
        if (result.isEmpty()) {
            throw new HashNotFoundException();
        }

        return formatLastRecord(result);
    }

    @Override
    public List<AnalyzedData> getAll(int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be bigger than 1");
        }
        List<AnalyzedData> result = repository.findLast(amount);
        return result.isEmpty() ? result : formatLastRecord(result);
    }

    @Override
    public List<AnalyzedData> getAllWithHash(String hash, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be bigger than 1");
        }
        List<AnalyzedData> resultList = repository.findLastByHash(hash, amount);
        if (resultList.isEmpty()) {
            throw new HashNotFoundException();
        }
        return formatLastRecord(resultList);
    }

    private static List<AnalyzedData> formatLastRecord(List<AnalyzedData> result) {
        int size = result.size();

        if(size > 0) {
            AnalyzedData lastRecord = result.get(result.size() - 1);
            AnalyzedData preLastRecord = size > 2 ? result.get(result.size() - 2) : null;

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
