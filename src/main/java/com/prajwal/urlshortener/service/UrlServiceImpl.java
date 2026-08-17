package com.prajwal.urlshortener.service;

import com.prajwal.urlshortener.dto.UrlRequest;
import com.prajwal.urlshortener.dto.UrlResponse;
import com.prajwal.urlshortener.dto.UrlStatsResponse;
import com.prajwal.urlshortener.entity.Url;
import com.prajwal.urlshortener.exception.UrlNotFoundException;
import com.prajwal.urlshortener.repository.UrlRepository;
import com.prajwal.urlshortener.util.ShortCodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;

    public UrlServiceImpl(
            UrlRepository urlRepository,
            ShortCodeGenerator shortCodeGenerator) {

        this.urlRepository = urlRepository;
        this.shortCodeGenerator = shortCodeGenerator;
    }

    @Override
    public UrlResponse createShortUrl(UrlRequest request) {

        String shortCode = generateUniqueShortCode();

        Url url = new Url();

        url.setOriginalUrl(request.getUrl());
        url.setShortCode(shortCode);

        LocalDateTime now = LocalDateTime.now();

        url.setCreatedAt(now);
        url.setUpdatedAt(now);
        url.setAccessCount(0L);

        Url savedUrl = urlRepository.save(url);

        return convertToUrlResponse(savedUrl);
    }

    @Override
    public UrlResponse getUrl(String shortCode) {

        Url url = findByShortCode(shortCode);

        return convertToUrlResponse(url);
    }

    @Override
    public UrlResponse updateUrl(
            String shortCode,
            UrlRequest request) {

        Url url = findByShortCode(shortCode);

        url.setOriginalUrl(request.getUrl());
        url.setUpdatedAt(LocalDateTime.now());

        Url updatedUrl = urlRepository.save(url);

        return convertToUrlResponse(updatedUrl);
    }

    @Override
    public void deleteUrl(String shortCode) {

        Url url = findByShortCode(shortCode);

        urlRepository.delete(url);
    }

    @Override
    public UrlStatsResponse getStatistics(String shortCode) {

        Url url = findByShortCode(shortCode);

        return convertToStatsResponse(url);
    }

    @Override
    @Transactional
    public String getOriginalUrlAndIncrementAccess(
            String shortCode) {

        Url url = findByShortCode(shortCode);

        Long currentCount = url.getAccessCount();

        url.setAccessCount(currentCount + 1);

        urlRepository.save(url);

        return url.getOriginalUrl();
    }

    private Url findByShortCode(String shortCode) {

        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException(
                                "No URL found for short code: "
                                        + shortCode
                        )
                );
    }

    private String generateUniqueShortCode() {

        String shortCode = shortCodeGenerator.generateShortCode();

        while (urlRepository.existsByShortCode(shortCode)) {

            shortCode = shortCodeGenerator.generateShortCode();
        }

        return shortCode;
    }

    private UrlResponse convertToUrlResponse(Url url) {

        return new UrlResponse(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortCode(),
                url.getCreatedAt(),
                url.getUpdatedAt()
        );
    }

    private UrlStatsResponse convertToStatsResponse(Url url) {

        return new UrlStatsResponse(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortCode(),
                url.getCreatedAt(),
                url.getUpdatedAt(),
                url.getAccessCount()
        );
    }
}