package org.example.service.DataService;

import org.example.model.RawData;

import java.util.List;

public interface DataService {
    List<RawData> getAll();
    List<RawData> getAllWithHash(String hash);
//    List<RawData> getAll(int amount); - это те же методы, но с ограничением по числу записей.
                                    //    В задании не было указано об такой необходимости,
                                    //    но я решил что они уместны. Пока они "заморожены"
//    List<RawData> getAllWithHash(String hash, int amount);
}
