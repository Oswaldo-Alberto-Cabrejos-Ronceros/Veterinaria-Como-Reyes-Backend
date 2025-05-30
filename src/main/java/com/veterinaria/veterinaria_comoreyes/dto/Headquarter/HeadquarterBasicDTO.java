package com.veterinaria.veterinaria_comoreyes.dto.Headquarter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 Headquarter Basic (id + name):
 for select and request or response short in others methods
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeadquarterBasicDTO {

    private Long id;

    private String name;

}
