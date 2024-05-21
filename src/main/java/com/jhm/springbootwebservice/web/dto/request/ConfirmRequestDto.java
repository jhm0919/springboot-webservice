package com.jhm.springbootwebservice.web.dto.request;

import lombok.Data;

@Data
public class ConfirmRequestDto {
    private String email;
    private String code;
}
