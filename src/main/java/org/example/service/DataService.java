package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.DataEntity;
import org.example.model.User;
import org.example.repository.DataRepository;
import org.example.util.exceptions.HashNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataService {
    private final DataRepository repository;

    public List<DataEntity> getAll() {
        return repository.findAll();
    }

    public List<DataEntity> getAll(int amount) {
        if (amount <  1) {
            throw new IllegalArgumentException("Amount must be bigger than 1");
        }
        List<DataEntity> result = repository.findLast(amount);
        return result.isEmpty() ? result : formatLastRecord(result);
    }

    public List<DataEntity> getAllByUser(User user, Date startDate, Date endDate) {
        List<DataEntity> result = repository.findAllByUser(user.getId());
        List<DataEntity> toReturn = filterByTimeConstraints(startDate, endDate, result);
        return toReturn.isEmpty() ? toReturn : formatLastRecord(toReturn);
    }

    public List<DataEntity> getAllWithHash(String hash) {
        List<DataEntity> result = repository.findAllByHash(hash);
        if (result.isEmpty()) {
            throw new HashNotFoundException();
        }

        return formatLastRecord(result);
    }

    public List<DataEntity> getAllWithHash(String hash, User user, Date startDate, Date endDate) {
        List<DataEntity> resultFromDb = repository.findAllByHash(hash, user.getId());
        List<DataEntity> result = filterByTimeConstraints(startDate, endDate, resultFromDb);
        if (result.isEmpty()) {
            throw new HashNotFoundException();
        }

        return formatLastRecord(result);
    }

    private static List<DataEntity> filterByTimeConstraints(Date startDate, Date endDate, List<DataEntity> resultFromDb) {
        return resultFromDb.stream()
                .filter(dataEntity -> {
                    if (startDate != null) {
                        return dataEntity.getTime().isAfter(startDate.toInstant());
                    }
                    return true;
                })
                .filter(dataEntity -> {
                    if (endDate != null) {
                        return dataEntity.getTime().isBefore(endDate.toInstant());
                    }
                    return true;
                }).collect(Collectors.toList());
    }

    public List<DataEntity> getAll(int amount, User user, Date startDate, Date endDate) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be bigger than 0");
        }
        List<DataEntity> result = repository.findLast(amount, user.getId());
        List<DataEntity> toReturn = filterByTimeConstraints(startDate, endDate, result);

        return toReturn.isEmpty() ? toReturn : formatLastRecord(toReturn);
    }

    public List<DataEntity> getAllWithHash(String hash, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be bigger than 0");
        }
        List<DataEntity> resultList = repository.findLastByHash(hash, amount);
        if (resultList.isEmpty()) {
            throw new HashNotFoundException();
        }
        return formatLastRecord(resultList);
    }

    public List<DataEntity> getAllWithHash(String hash, int amount, User user, Date startDate, Date endDate) {
        if (amount < 2) {
            throw new IllegalArgumentException("Amount must be bigger than 1");
        }
        List<DataEntity> resultList = repository.findLastByHash(hash, amount, user.getId());
        List<DataEntity> toReturn = filterByTimeConstraints(startDate, endDate, resultList);
        if (toReturn.isEmpty()) {
            return toReturn;
        }
        return formatLastRecord(toReturn);
    }

    private static List<DataEntity> formatLastRecord(List<DataEntity> result) {
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
