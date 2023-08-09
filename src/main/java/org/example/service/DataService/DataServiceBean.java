package org.example.service.DataService;

import lombok.RequiredArgsConstructor;
import org.example.model.RawData;
import org.example.repository.RawDataRepository;
import org.example.util.exceptions.HashNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DataServiceBean implements DataService {
    private final RawDataRepository repository;
    @Override
    public List<RawData> getAll() {
        List<RawData> result = repository.findAllFiltered();

        return formatLastRecord(result);
    }

    @Override
    public List<RawData> getAllWithHash(String hash) {
        List<RawData> result = repository.findAllByHash(hash);
        if (result.isEmpty()) {
            throw new HashNotFoundException();
        }

        return formatLastRecord(result);
    }

    @Override
    public List<RawData> getAll(int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount must be bigger than 0");
        }
        return formatLastRecord(repository.findLast(amount));
    }

    @Override
    public List<RawData> getAllWithHash(String hash, int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount must be bigger than 0");
        }
        List<RawData> resultList = repository.findLastByHash(hash, amount);
        if (resultList.isEmpty()) {
            throw new HashNotFoundException();
        }
        return formatLastRecord(resultList);
    }

    private static List<RawData> formatLastRecord(List<RawData> result) {
        RawData last = result.get(result.size() - 1);
        int i = result.size() - 1;
        for (; i > 1; i--) {
            if(!result.get(i).getHash().equals(result.get(i - 1).getHash())) {
                break;
            }
        }
        RawData relativeLast = result.get(i);
        Duration duration = Duration.between(relativeLast.getTime(), Instant.now().atOffset(ZoneOffset.UTC));
        last.setExpectedDuration(
                LocalTime.parse(
                        duration.toHours() + ":" + duration.toMinutesPart() + ":" + duration.toSecondsPart(),
                        DateTimeFormatter.ofPattern("H:m:s")
                ).format(DateTimeFormatter.ofPattern("HH:mm:ss."))
        );

        return result;
    }
}
