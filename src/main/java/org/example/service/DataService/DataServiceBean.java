package org.example.service.DataService;

import lombok.RequiredArgsConstructor;
import org.example.model.RawData;
import org.example.repository.RawDataRepository;
import org.example.util.exceptions.HashNotFoundException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataServiceBean implements DataService {
    private final RawDataRepository repository;

    @Override
    public List<RawData> getAll() {
        List<RawData> result = repository.findAllFiltered();

        return formatLastRecordV2(result);
    }

    @Override
    public List<RawData> getAllWithHash(String hash) {
        List<RawData> result = repository.findAllByHash(hash);
        if (result.isEmpty()) {
            throw new HashNotFoundException();
        }

        return formatLastRecordV2(result);
    }

    @Override
    public List<RawData> getAll(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be bigger than 0");
        }
        return formatLastRecordV2(repository.findLast(amount));
    }

    @Override
    public List<RawData> getAllWithHash(String hash, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be bigger than 0");
        }
        List<RawData> resultList = repository.findLastByHash(hash, amount);
        if (resultList.isEmpty()) {
            throw new HashNotFoundException();
        }
        return formatLastRecordV2(resultList);
    }

    private static List<RawData> formatLastRecord(List<RawData> result) { // этот метод переделывает последнюю строку. через него проходят все другие
        RawData last = result.get(result.size() - 1);
        int i = result.size() - 1;
        for (; i > 1; i--) {
            if (!result.get(i).getHash().equals(result.get(i - 1).getHash())) { // итерируемся, пока не дойдем до того момента, где отличный от нашего хеш
                break;
            }
        }
        RawData relativeLast = result.get(i);
        Duration duration = Duration.between(relativeLast.getTime(), Instant.now().atOffset(ZoneOffset.UTC));
        last.setExpectedDuration(
                LocalTime.parse(
                        duration.toHours() + ":" + duration.toMinutesPart() + ":" + duration.toSecondsPart(),
                        DateTimeFormatter.ofPattern("H:m:s")
                ).format(DateTimeFormatter.ofPattern("HH:mm:ss."))  //согласен, не лучший способ подсчета времени для последнего элемента,
                                                                    // но так как нам это нужно сделать всего 1 раз, думаю не сильно страшно по производительности
        );

        return result;
    }

    private static List<RawData> formatLastRecordV2(List<RawData> result) { //Более оптимизированая версия, не использует циклы. Интересно у Вас узнать, какая лучше
        RawData last = result.get(result.size() - 1);
        Instant relativeLastTime;
        if (result.size() >= 2 && result.get(result.size() - 2).getHash().equals(last.getHash())) {
            RawData preLast = result.get(result.size() - 2);
            String[] duration = preLast.getExpectedDuration().split("\\.")[0].split(":");
            Duration extractedDuration = Duration.ofHours(Long.parseLong(duration[0])).plusMinutes(Long.parseLong(duration[1])).plusSeconds(Long.parseLong(duration[2]));
            relativeLastTime = last.getTime().minus(extractedDuration);

        } else {
            relativeLastTime = last.getTime();
        }
        Duration duration = Duration.between(relativeLastTime, Instant.now().atOffset(ZoneOffset.UTC));
        String durationString;
        if(duration.toDaysPart() != 0) {
            durationString = String.format("%d day %02d:%02d:%02d.", duration.toDaysPart(), duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
        } else {
            durationString = String.format("%02d:%02d:%02d.", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
        }

        last.setExpectedDuration(
                durationString
        );

        return result;
    }
}
