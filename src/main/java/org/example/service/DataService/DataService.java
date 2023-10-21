package org.example.service.DataService;

import org.example.model.Data;

import java.util.List;

public interface DataService {
    List<Data> getAll();
    List<Data> getAllWithHash(String hash);
    List<Data> getAll(int amount); // - это те же методы, но с ограничением по числу записей.
    List<Data> getAllWithHash(String hash, int amount);
}
