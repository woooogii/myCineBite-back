package com.cine.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.cine.back.movieList.response.BoxOfficeMovieResponse;
import com.cine.back.movieList.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BoxOfficeConfig {

    @Value("${boxOffice.boxOffice-url}")
    private String boxOfficeUrl;
    @Value("${boxOffice.boxOffice-api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public BoxOfficeMovieResponse getBoxOfficeData() {
        log.info("api - 영화진흥위원회 주간/주말별 인기 상영작 조회 시작");
        String targetDt = DateUtils.getPreviousSundayDate(); // 날짜 설정

        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(boxOfficeUrl)
                .queryParam("key", apiKey)
                .queryParam("itemPerPage", 10)
                .queryParam("targetDt", targetDt)
                .build();

        String url = uriComponents.toUriString();
        log.info("api - 영화진흥위원회 주간/주말별 인기 상영작 조회 url: {}", url);
        return restTemplate.getForObject(url, BoxOfficeMovieResponse.class);
    }
}
