package com.prajwal.urlshortener.service;

import com.prajwal.urlshortener.dto.UrlRequest;
import com.prajwal.urlshortener.dto.UrlResponse;
import com.prajwal.urlshortener.dto.UrlStatsResponse;

public interface UrlService {

    UrlResponse createShortUrl(UrlRequest request);

    UrlResponse getUrl(String shortCode);

    UrlResponse updateUrl(String shortCode, UrlRequest request);

    void deleteUrl(String shortCode);

    UrlStatsResponse getStatistics(String shortCode);

    String getOriginalUrlAndIncrementAccess(String shortCode);
}