package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter_Service.HeadquarterVetServiceDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.HeadquarterVetService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = GlobalMapperConfig.class, uses = {VeterinaryServiceMapper.class},componentModel = "spring")
public interface HeadquarterVetServiceMapper {

    @Mapping(source = "headquarter.headquarterId", target = "headquarterId")
    @Mapping(source = "veterinaryService", target = "service")
    HeadquarterVetServiceDTO mapToHeadquarterVetServiceDTO(HeadquarterVetService entity);

    @Mapping(source = "headquarterId", target = "headquarter", qualifiedByName = "mapHeadquarterFromId")
    @Mapping(source = "service", target = "veterinaryService")
    HeadquarterVetService mapToHeadquarterVetService(HeadquarterVetServiceDTO dto);

    @Named("mapHeadquarterFromId")
    static Headquarter mapHeadquarterFromId(Long id) {
        Headquarter h = new Headquarter();
        h.setHeadquarterId(id);
        return h;
    }
}
