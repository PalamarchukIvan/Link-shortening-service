package org.example.service.DataService;

import lombok.RequiredArgsConstructor;
import org.example.model.RawData;
import org.example.repository.RawDataRepository;
import org.example.util.exceptions.HashNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DataServiceBean implements DataService {
    private final RawDataRepository repository;
    @Override
    public List<RawData> getAll() {
        return repository.findAllFiltered();
    }

    @Override
    public List<RawData> getAllWithHash(String hash) {
        List<RawData> resultList = repository.findAllByHash(hash);
        if (resultList.isEmpty()) {
            throw new HashNotFoundException();
        }
        return resultList;
    }

    @Override
    public List<RawData> getAll(int amount) {
        return repository.findLast(amount);
    }

    @Override
    public List<RawData> getAllWithHash(String hash, int amount) {
        List<RawData> resultList = repository.findAllByHash(hash, amount);
        if (resultList.isEmpty()) {
            throw new HashNotFoundException();
        }
        return resultList;
    }
}
