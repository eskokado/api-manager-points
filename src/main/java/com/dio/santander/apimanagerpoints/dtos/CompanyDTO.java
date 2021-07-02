package com.dio.santander.apimanagerpoints.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    @NotNull
    private long id;

    @NotEmpty
    private String description;

    @Size(min = 14, max = 14)
    private String cnpj;

    @NotEmpty
    private String address;

    @NotEmpty
    private String district;

    @NotEmpty
    private String city;

    @Size(min = 2, max = 3)
    private String state;

    @NotEmpty
    private String phone;
}
