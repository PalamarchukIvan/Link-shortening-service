package org.example.service.DataService;

import lombok.RequiredArgsConstructor;
import org.example.model.RawData;
import org.example.repository.RawDataRepository;
import org.example.util.exceptions.HashNotFoundException;
import org.example.util.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DataServiceBean implements DataService {
    private final RawDataRepository repository;
    @Override
    /*
      Я решил сделеть фильтрацию первых элементов у
      которых поле excpectedDuration == null.
      В прошлый раз Вы сказали мне, что в угоду оптимизации ранними записями можно принебречь.
      Я посчитал что вывод таких записей при просмотре статистики - неуместен, а беспокоить вас по такому поводу не стал
      **/
    public List<RawData> getAll() {
        List<RawData> resultList = repository.findAll(); //это можно было сделать через стримы, но я решил,
                                                        // что обходить потенцеально тысячи записей будет неоправданно дорогой операцией
                                                        //По этому, учитывая что null-ы могут быть только в первых записях я сделал эту фильтрацию следующим образом
        int i = 0;
        for (RawData data : resultList) {
            if(data.getExpectedDuration() != null) {
                break;
            }
            i++;
        }
        return resultList.subList(i, resultList.size());
    }

    @Override
    public List<RawData> getAllWithHash(String hash) {
        List<RawData> resultList = repository.findAllByHash(hash);
        if (resultList.isEmpty()) {
            throw new HashNotFoundException();
        }
        int i = 0;
        for (RawData data : resultList) {
            if(data.getExpectedDuration() != null) {
                break;
            }
            i++;
        }
        return resultList.subList(i, resultList.size());
    }
    //Их реализация
//    @Override
//    public List<RawData> getAll(int amount) {
//        return getAll().subList(0, amount);

//    }

//    @Override
//    public List<RawData> getAllWithHash(String hash, int amount) {
//        return getAllWithHash(hash).subList(0, amount);
//    }
}
