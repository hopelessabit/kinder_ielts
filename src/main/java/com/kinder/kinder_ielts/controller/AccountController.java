package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.service.base.BaseAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final BaseAccountService baseAccountService;
//
//    @GetMapping("{id}")
//    public ResponseEntity<ResponseData<AccountResponse>> getAccount(@PathVariable String id){
//        return ResponseUtil.getResponse(baseAccountService.getAccountById(id, false), AccountMessage.FOUND_SUCCESSFULLY);
//    }
}
