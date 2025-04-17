package com.veterinaria.veterinaria_comoreyes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PermissionDTO {
    private long permissionId;
    private String actionCode;
    private String module;
    private String description;
}
