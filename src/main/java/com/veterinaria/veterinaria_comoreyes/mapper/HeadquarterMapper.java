package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.HeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;

public class HeadquarterMapper {

    public static HeadquarterDTO maptoHeadquarterDTO(Headquarter headquarter) {
        return new HeadquarterDTO(
                headquarter.getHeadquarterId(),
                headquarter.getPhone(),
                headquarter.getAddress(),
                headquarter.getEmail(),
                headquarter.getDistrict(),
                headquarter.getProvince(),
                headquarter.getDepartment(),
                headquarter.getName()

        );
    }

    public static Headquarter maptoHeadquarter(HeadquarterDTO headquarterDTO) {
        return new Headquarter(
                headquarterDTO.getHeadquarterId(),
                headquarterDTO.getPhone(),
                headquarterDTO.getAddress(),
                headquarterDTO.getEmail(),
                headquarterDTO.getDistrict(),
                headquarterDTO.getProvince(),
                headquarterDTO.getDepartment(),
                headquarterDTO.getName()
        );
    }
}
