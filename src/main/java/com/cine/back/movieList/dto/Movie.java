package com.cine.back.movieList.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Movie {

    @JsonProperty("id")
    private int movieId;

    private List<Genre> genres;
    private List<Countries> countries;

    @Column(name = "fresh_count")
    private int freshCount;

    @Column(name = "rotten_count")
    private int rottenCount;

    @Column(name = "tomato_score")
    private double tomatoScore; // 평가 퍼센티지

    @JsonProperty("vote_average")
    private float voteAverage;

    // 지금은 트렌드 영화 목록 받고 -> movieId만 사용해서 디테일에 참조
    // 나중에 트렌드 영화 목록 출력할 때 필요
    // @JsonProperty("poster_path")
    // @Column(length = 100)
    // private String posterPath;

    // @JsonProperty("title")
    // @Column(length = 100)
    // private String title;

    // @JsonProperty("overview")
    // @Column(columnDefinition = "TEXT")
    // private String overview;

    // @JsonProperty("release_date")
    // @Column(length = 20)
    // private String releaseDate;

    // @JsonProperty("popularity")
    // @Column(length = 20)
    // private String popularity;

}
