//package com.jhm.springbootwebservice.validation;
//
//import com.jhm.springbootwebservice.config.auth.dto.UserRequestDto;
//import com.jhm.springbootwebservice.util.RedisUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class CheckConfirmValidator extends AbstractValidator<UserRequestDto> {
//
//    private final RedisUtil redisUtil;
//
//    @Override
//    protected void doValidate(UserRequestDto dto, Errors errors) {
//        String requestCode = dto.getCode();
//        log.info("넘어온 인증번호={}", requestCode);
//        String storageCode = redisUtil.getData(dto.getEmail());
//        log.info("진짜 인증번호={}", storageCode);
//        if (requestCode != "" && !requestCode.equals(storageCode)) { // 인증번호가 다르면
//            errors.rejectValue("code", "인증번호 불일치", "인증번호가 다릅니다.");
//        }
//    }
//}
