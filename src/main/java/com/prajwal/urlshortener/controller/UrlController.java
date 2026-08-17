package com.prajwal.urlshortener.controller;

import com.prajwal.urlshortener.dto.UrlRequest;
import com.prajwal.urlshortener.dto.UrlResponse;
import com.prajwal.urlshortener.dto.UrlStatsResponse;
import com.prajwal.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    // ==========================================
    // CREATE SHORT URL
    // POST /shorten
    // ==========================================

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> createShortUrl(
            @Valid @RequestBody UrlRequest request) {

        UrlResponse response =
                urlService.createShortUrl(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // ==========================================
    // GET URL INFORMATION
    // GET /shorten/{shortCode}
    // ==========================================

    @GetMapping("/shorten/{shortCode}")
    public ResponseEntity<UrlResponse> getUrl(
            @PathVariable String shortCode) {

        UrlResponse response =
                urlService.getUrl(shortCode);

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // UPDATE URL
    // PUT /shorten/{shortCode}
    // ==========================================

    @PutMapping("/shorten/{shortCode}")
    public ResponseEntity<UrlResponse> updateUrl(
            @PathVariable String shortCode,
            @Valid @RequestBody UrlRequest request) {

        UrlResponse response =
                urlService.updateUrl(shortCode, request);

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // DELETE URL
    // DELETE /shorten/{shortCode}
    // ==========================================

    @DeleteMapping("/shorten/{shortCode}")
    public ResponseEntity<Void> deleteUrl(
            @PathVariable String shortCode) {

        urlService.deleteUrl(shortCode);

        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // GET STATISTICS
    // GET /shorten/{shortCode}/stats
    // ==========================================

    @GetMapping("/shorten/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponse> getStatistics(
            @PathVariable String shortCode) {

        UrlStatsResponse response =
                urlService.getStatistics(shortCode);

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // REDIRECT
    // GET /{shortCode}
    // ==========================================

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(
            @PathVariable String shortCode) {

        String originalUrl =
                urlService.getOriginalUrlAndIncrementAccess(
                        shortCode
                );

        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(URI.create(originalUrl));

        return new ResponseEntity<>(
                headers,
                HttpStatus.MOVED_PERMANENTLY
        );
    }
}