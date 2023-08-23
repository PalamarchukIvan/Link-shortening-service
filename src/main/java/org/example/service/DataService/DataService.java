package org.example.service.DataService;

import org.example.model.AnalyzedData;

import java.util.List;

public interface DataService {
    List<AnalyzedData> getAll();
    List<AnalyzedData> getAllWithHash(String hash);
    List<AnalyzedData> getAll(int amount); // - это те же методы, но с ограничением по числу записей.
    List<AnalyzedData> getAllWithHash(String hash, int amount);
}
