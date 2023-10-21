package org.example.service.DataService;

import org.example.model.DataEntity;

import java.util.List;

public interface DataService {
    List<DataEntity> getAll();
    List<DataEntity> getAllWithHash(String hash);
    List<DataEntity> getAll(int amount); // - это те же методы, но с ограничением по числу записей.
    List<DataEntity> getAllWithHash(String hash, int amount);
}
