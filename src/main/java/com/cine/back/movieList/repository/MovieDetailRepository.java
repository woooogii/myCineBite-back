package com.cine.back.movieList.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cine.back.movieList.entity.MovieDetailEntity;

public interface MovieDetailRepository extends JpaRepository<MovieDetailEntity, Integer> {

        Optional<List<MovieDetailEntity>> findAllByOrderByPopularityAsc();

        Optional<MovieDetailEntity> findByMovieId(int movieId);

        @Query("SELECT MD FROM movie_details MD JOIN MD.genres MG WHERE MG.name IN :genres GROUP BY MD.movieId HAVING COUNT(DISTINCT MG.name) = :genresCount ORDER BY popularity DESC")
        Optional<List<MovieDetailEntity>> findByGenres(@Param("genres") List<String> genres,
                        @Param("genresCount") int genresCount);

        @Query("SELECT md FROM movie_details md JOIN md.credits.cast c WHERE c.name = :actor")
        Optional<List<MovieDetailEntity>> findByActors(@Param("actor") String actor);

        // 영화명, 배우, 장르로 검색
        List<MovieDetailEntity> findByTitleContainingOrCredits_Cast_NameOrGenres_Name(String title,
                        String castName, String genreName);

        // 비슷한 장르의 영화 추천
        public List<MovieDetailEntity> findByGenres_NameOrderByReleaseDateDesc(String genre);

        Optional<MovieDetailEntity> findByTitle(String movieNm);

}
