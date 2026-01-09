package com.caseclarity.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestController {

    @GetMapping("/secure/user/test")
    public String user() {
        return "USER_OK";
    }

    @GetMapping("/secure/admin/test")
    public String admin() {
        return "ADMIN_OK";
    }

    @GetMapping("/internal/auth/login")
    public String publicEndpoint() {
        return "PUBLIC_OK";
    }
}
