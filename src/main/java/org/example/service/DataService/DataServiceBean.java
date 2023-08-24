package org.example.service.DataService;

import lombok.RequiredArgsConstructor;
import org.example.model.AnalyzedData;
import org.example.model.RawDataLight;
import org.example.repository.DataRepository;
import org.example.repository.RawDataRepository;
import org.example.util.exceptions.HashNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataServiceBean implements DataService {
    private final DataRepository dataRepository;
    private final RawDataRepository rawDataRepository;

    @Override
    public List<AnalyzedData> getAll() {
        List<AnalyzedData> result = dataRepository.findAllRaws();
        return result.isEmpty() ? result : formatLastRecord(result);
    }

    @Override
    public List<AnalyzedData> getAllWithHash(String hash) {
        List<AnalyzedData> result = dataRepository.findAllByHash(hash);
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
        List<AnalyzedData> result = dataRepository.findLast(amount);
        return result.isEmpty() ? result : formatLastRecord(result);
    }

    @Override
    public List<AnalyzedData> getAllWithHash(String hash, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be bigger than 1");
        }
        List<AnalyzedData> resultList = dataRepository.findLastByHash(hash, amount);
        if (resultList.isEmpty()) {
            throw new HashNotFoundException();
        }
        return formatLastRecord(resultList);
    }

    private List<AnalyzedData> formatLastRecord(List<AnalyzedData> result) {
        int size = result.size();

        if(size > 0) {
            AnalyzedData lastRecord = result.get(result.size() - 1);
            if(lastRecord.getExpectedDuration() == null) {
                RawDataLight analogy = rawDataRepository.findById(lastRecord.getTime());
                AnalyzedData preLastRecord = size > 2 ? result.get(result.size() - 2) : null;
                long calculatedDuration;
                if (preLastRecord != null && lastRecord.getHash().equals(preLastRecord.getHash()) && analogy.getPrevTime().equals(preLastRecord.getTime())) {
                     calculatedDuration = Instant.now().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() - lastRecord.getTime().toEpochMilli() + preLastRecord.getExpectedDuration();
                } else {
                    calculatedDuration = Instant.now().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() - lastRecord.getTime().toEpochMilli();
                }
                lastRecord.setExpectedDuration(calculatedDuration);
            }
        }

        return result;
    }
}
