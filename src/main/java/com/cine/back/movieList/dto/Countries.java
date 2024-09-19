package com.cine.back.movieList.dto;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Countries {
    private String iso_3166_1;
    private String name;
}
