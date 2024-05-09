package com.jhm.springbootwebservice.web.dto.request;

import lombok.Data;

@Data
public class ConfirmRequestDto {
    String email;
    String code;
}
