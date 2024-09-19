package com.cine.back.movieList.entity;

import java.util.List;

import com.cine.back.movieList.dto.Countries;
import com.cine.back.movieList.dto.Credits;
import com.cine.back.movieList.dto.Genre;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "movie_details")
public class MovieDetailEntity {

    @Id
    @JsonProperty("id")
    @Column(nullable = false, name = "movie_id")
    private int movieId;

    @NotNull
    @JsonProperty("title")
    @Column(nullable = false, name = "title")
    private String title;

    @NotNull
    @JsonProperty("release_date")
    @Column(nullable = false, length = 20)
    private String releaseDate;

    @NotNull
    @JsonProperty("overview")
    @Column(columnDefinition = "TEXT")
    private String overview;

    @NotNull
    @JsonProperty("poster_path")
    @Column(nullable = false, length = 100)
    private String posterPath;

    @NotNull
    @JsonProperty("popularity")
    @Column(nullable = false, length = 20)
    private int popularity;

    @NotNull
    @JsonProperty("runtime")
    @Column(nullable = false, length = 100)
    private String runtime;

    @NotNull
    @JsonProperty("genres")
    @ElementCollection
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    private List<Genre> genres;

    @NotNull
    @JsonProperty("credits")
    private Credits credits;

    @NotNull
    @Column(nullable = false, name = "fresh_count")
    private int freshCount;

    @NotNull
    @Column(nullable = false, name = "rotten_count")
    private int rottenCount;

    @NotNull
    @Column(nullable = false, name = "tomato_score")
    private double tomatoScore; // 평가 퍼센티지

    @NotNull
    @JsonProperty("production_countries")
    @ElementCollection
    @CollectionTable(name = "production_countries", joinColumns = @JoinColumn(name = "movie_id"))
    private List<Countries> countries;

}