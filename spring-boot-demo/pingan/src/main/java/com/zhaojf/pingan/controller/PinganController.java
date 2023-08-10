package com.zhaojf.pingan.controller;

import com.zhaojf.pingan.entity.User;
import com.zhaojf.pingan.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pingan")
public class PinganController {

    private final UserService userService;

    public PinganController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping("/token")
//    public JSONObject setToken(@RequestBody TokenInfo tokenInfo){
//
//        Task.sessionId = tokenInfo.getSessionId();
//        Task.signature = tokenInfo.getSignature();
//        JSONObject result = new JSONObject();
//        result.put("sessionId",Task.sessionId);
//        result.put("signature",Task.signature);
//        return result;
//    }

    @GetMapping("/token")
    public List<User> getToken(){

        return userService.list();
    }

}
