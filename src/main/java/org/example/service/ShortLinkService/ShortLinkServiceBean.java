package org.example.service.ShortLinkService;

import lombok.RequiredArgsConstructor;
import org.example.model.RawDataLight;
import org.example.model.ShortLink;
import org.example.repository.RawDataRepository;
import org.example.repository.ShortLinkRepository;
import org.example.util.exceptions.ResourceDeletedException;
import org.example.util.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ShortLinkServiceBean implements ShortLinkService {
    private final ShortLinkRepository repository;
    private final RawDataRepository dataRepository;

    @Override
    public ShortLink create(ShortLink link) {
        link.setHash(getHashCode());
        return repository.save(link);
    }

    @Override
    public ShortLink getByHash(String hash) {
        ShortLink foundLink = repository.findById(hash).orElseThrow(ResourceNotFoundException::new);
        if (foundLink.isDeleted()) {
            throw new ResourceDeletedException();
        }
        return foundLink;
    }

    @Override
    public ShortLink deleteByHash(String hash) {
        ShortLink toDeleteLink = getByHash(hash);
        toDeleteLink.setDeleted(true);
        repository.save(toDeleteLink);
        return toDeleteLink;
    }

    @Override
    public String getHashCode() {
        return getHashV4();
    }
    @Deprecated
    private String getHashV2(ShortLink link) {
        String hash = String.valueOf(link.getLink().hashCode());
        int length = (int) (Math.random() * 3);
        StringBuilder hashBuilder = new StringBuilder();
        for (int i = 0; i < 3; i++) { //записываем буквы
            if (Math.random() > 0.5) {
                hashBuilder.append((char) ((int) (Math.random() * 25) + 65)); //Случайная буква верхнего регистра
            } else {
                hashBuilder.append((char) ((int) (Math.random() * 25) + 97)); //Случайная буква нижнего регистра
            }
        }
        for (int i = 0; i < length; i++) {//записываем цифры (может быть от 0 до 2 цифр)
            hashBuilder.append(hash.charAt(hash.length() - i - 1));
        }
        return hashBuilder.toString();
    }
    @Deprecated
    private String getHashV3(ShortLink link) {
        String hash = String.valueOf(Math.abs(link.getLink().hashCode()));
        int length = 3 + (int) (Math.random() * 3);
        StringBuilder hashBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) { //записываем буквы
            double randomDouble = Math.random();
            if (randomDouble > (2F/3F)) {
                hashBuilder.append((char) ((int) (Math.random() * 26) + 65)); //Случайная буква верхнего регистра
            } else if (randomDouble > (1F/3F)) {
                hashBuilder.append((char) ((int) (Math.random() * 26) + 97)); //Случайная буква нижнего регистра
            } else {
                hashBuilder.append(hash.charAt((int) (Math.random() * (hash.length())))); //берем случайное число из хеша
            }
        }
        String result = hashBuilder.toString();
        return repository.checkIfUniqueHash(result) == 0 ? result : getHashV3(link); //Если это уникальный хеш, то мы вернем result. Если нет - рекурсивно пойдем пересоздавать новый
    }

    private String getHashV4() {
        Random random = new Random();
        String result;
        StringBuilder hashBuilder;
        int counter = 0;
        int length = 3;
        do {
            hashBuilder = new StringBuilder();
            for (int i = 0; i < length; i++) { //записываем буквы
                int symbol = random.nextInt(62);
                if(symbol < 26) {
                    hashBuilder.append((char) ('a' + symbol));
                } else if (symbol < 52){
                    hashBuilder.append((char) ('A' + symbol - 26));
                } else {
                    hashBuilder.append((char) ('0' + (symbol - 52)));
                }
            }
            result = hashBuilder.toString();
            counter++;
            if(counter % 10 == 0) { //Каждые 10 раз увеличиваем длину
                length++;
            }
            if(counter == 30) {
                throw new RuntimeException("hash was not able not be formed");
            }
        } while (repository.checkIfUniqueHash(result) != 0);
        return  result;
    }

    @Override
    public void updateOnStatistics(Duration duration, String hash, boolean isFound) {
        RawDataLight newRecord = RawDataLight.builder()
                .time(Instant.now().atOffset(ZoneOffset.UTC).toInstant())
                .hash(hash)
                .lag(duration.toMillis())
                .isFound(isFound)
                .build();
        dataRepository.save(newRecord);
    }
    public void generateDataRecords(int amount) {
        Random random = new Random();
        int N = 100;
        String[] hashes = new String[N];

        for (int i = 0; i < N; i++) {
            hashes[i] = getHashV4();
        }
        int time = 0;
        for (int i = 0; i < amount; i++) {
            int length = random.nextInt(10);
            String hash = hashes[random.nextInt(N)];
            for (int j = 0; j < length; j++, time += 4000) {
                time += random.nextInt(10000);
                RawDataLight newRecord = RawDataLight.builder()
                        .time(Instant.ofEpochMilli(time))
                        .hash(hash)
                        .lag(0)
                        .isFound(true)
                        .build();
                dataRepository.save(newRecord);
            }
            amount-= length;
        }
    }
}
