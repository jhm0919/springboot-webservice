package com.jhm.springbootwebservice.web.dto.request;

import lombok.Data;

@Data
public class UserSearchDto {
    private String searchKeyword;
    private String searchType;
//    private String postType;
}
