package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.ShortLink;
import org.example.repository.ShortLinkRepository;
import org.example.util.exceptions.ResourceDeletedException;
import org.example.util.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShortLinkServiceBean implements  ShortLinkService {
    private final ShortLinkRepository repository;

    @Override
    public ShortLink create(ShortLink link) {
        if(!link.getLink().contains("http")) {
            link.setLink("https://".concat(link.getLink()));
        }
        link.setHash(getHashCode(link));
        return repository.save(link);
    }

    @Override
    public ShortLink getById(Long id) {
        ShortLink foundLink = repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        if(foundLink.isDeleted()) {
            throw new ResourceDeletedException();
        }
        return foundLink;
    }

    @Override
    public ShortLink deleteById(Long id) {
        ShortLink toDeleteLink = getById(id);
        toDeleteLink.setDeleted(true);
        repository.save(toDeleteLink);
        return toDeleteLink;
    }

    @Override
    public ShortLink getByHash(String hash) {
        return repository.findByHash(hash).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public String getHashCode(ShortLink link) {
        String input = link.getLink();
        try {
            // Получаем экземпляр MessageDigest с алгоритмом SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Конвертируем входную строку в массив байтов
            byte[] inputBytes = input.getBytes();

            // Вычисляем хеш
            byte[] hashBytes = md.digest(inputBytes);

            // Конвертируем байтовый хеш в строку
            StringBuilder hashCode = new StringBuilder();
            for (byte b : hashBytes) {
                // Преобразуем байт в положительное целое число и форматируем его как шестнадцатеричное число
                hashCode.append(String.format("%02X", b & 0xFF));
            }

            // Ограничиваем длину хешкода от 3 до 5 символов
            int length = (int)(Math.random() * 3) + 3;
            return hashCode.substring(0, length);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
//        return String.valueOf(link.getLink().hashCode());
    }
}
