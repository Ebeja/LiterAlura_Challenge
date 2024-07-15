package com.example.literAlura;

import com.example.literAlura.model.Book;
import com.example.literAlura.model.GutendexResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class GutendexService {

    private final RestTemplate restTemplate;
    private final String API_URL = "https://gutendex.com/books"; // Asegúrate de que esta URL es correcta

    public GutendexService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Book> fetchBooks(String query) {
        String url = API_URL + "?search=" + query;
        GutendexResponse response = restTemplate.getForObject(url, GutendexResponse.class);
        return response != null ? response.getResults() : Collections.emptyList();
    }
}
