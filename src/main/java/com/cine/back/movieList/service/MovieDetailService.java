package com.cine.back.movieList.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cine.back.config.MovieConfig;
import com.cine.back.movieList.dto.Movie;
import com.cine.back.movieList.entity.MovieDetailEntity;
import com.cine.back.movieList.repository.MovieDetailRepository;
import com.cine.back.movieList.response.MovieResponse;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MovieDetailService {
    private final MovieDetailRepository movieDetailRepository;
    private final MovieConfig movieConfig;

    public MovieDetailService(MovieDetailRepository movieDetailRepository, MovieConfig movieConfig) {
        this.movieDetailRepository = movieDetailRepository;
        this.movieConfig = movieConfig;
    }

    // 서버 실행 시 자동 저장
    @PostConstruct
    public void init() {
        try {
            getAllMovies();
            log.info("영화 api 데이터 저장 성공");
        } catch (Exception e) {
            log.error("에러 - api 데이터 자동 저장 실패", e);
        }
    }

    public List<Movie> fetchAllMovies() {
        List<Movie> allMovies = new ArrayList<>();

        for (int page = 1; page <= 10; page++) {
            try {
                MovieResponse allMovieResponse = movieConfig.fetchMovieList(page);
                if (allMovieResponse != null) {
                    List<Movie> movies = allMovieResponse.getResults();
                    allMovies.addAll(movies);
                    log.info("페이지 {}의 영화 {}개 가져오기 성공", page, movies.size());
                }
            } catch (Exception e) {
                log.error("에러 - api 영화 전체 데이터 조회 실패", e);
            }
        }
        return allMovies;
    }

    public void getAllMovies() {
        List<Movie> allMovies = fetchAllMovies();
        if (allMovies != null) {
            for (Movie movie : allMovies) {
                try {
                    MovieDetailEntity movieDetails = movieConfig.fetchMovieDetails(movie.getMovieId());
                    if (movieDetails != null) {
                        saveMovieDetail(movieDetails);
                        log.info("영화 ID {} 상세 정보 저장 성공", movie.getMovieId());
                    }
                } catch (Exception e) {
                    log.error("에러 - api 영화 상세 데이터 조회 실패", movie.getMovieId(), e);
                }
            }
        }
    }

    // 데이터 저장 (중복 방지)
    private void saveMovieDetail(MovieDetailEntity movieDetail) {
        try {
            Optional<MovieDetailEntity> optionalExistingMovie = movieDetailRepository
                    .findByMovieId(movieDetail.getMovieId());
            if (optionalExistingMovie.isPresent()) {
                MovieDetailEntity existingMovie = optionalExistingMovie.get();
                existingMovie.setTitle(movieDetail.getTitle());
                existingMovie.setOverview(movieDetail.getOverview());
                movieDetailRepository.save(existingMovie);
                log.debug("기존 영화 정보 수정 및 저장", existingMovie);
            } else {
                movieDetailRepository.save(movieDetail);
                log.debug("영화 상세정보 추가", movieDetail);
            }
        } catch (Exception e) {
            log.error("에러 - 저장 실패", movieDetail.getMovieId(), e);
        }
    }
}
