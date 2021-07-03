package com.dio.santander.apimanagerpoints.builders;

import com.dio.santander.apimanagerpoints.dtos.CompanyDTO;
import lombok.Builder;

@Builder
public class CompanyDTOBuilder {
    @Builder.Default
    private long id = 1L;

    @Builder.Default
    private String description = "Company Description 01";

    @Builder.Default
    private String cnpj = "12345678901234";

    @Builder.Default
    private String address = "Company Address 01";

    @Builder.Default
    private String district = "Company District 01";

    @Builder.Default
    private String city = "Company City 01";

    @Builder.Default
    private String state = "SP";

    @Builder.Default
    private String phone = "1234567890";

    public CompanyDTO toCompanyDTO() {
        return new CompanyDTO(
                id, description, cnpj, address, district, city, state, phone
        );
    }
}
