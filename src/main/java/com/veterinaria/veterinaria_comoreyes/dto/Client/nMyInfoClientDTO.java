package com.veterinaria.veterinaria_comoreyes.dto.Client;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterBasicDTO;
import com.veterinaria.veterinaria_comoreyes.dto.User.UserEmailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class nMyInfoClientDTO {
    private Long clientId;

    private UserEmailDTO user;

    private String dni;

    private String names;

    private String LastNames;

    private String phone;

    private String address;

    private HeadquarterBasicDTO headquarter;

}
