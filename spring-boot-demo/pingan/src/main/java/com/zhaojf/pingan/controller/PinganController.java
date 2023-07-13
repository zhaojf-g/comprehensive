package com.zhaojf.pingan.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhaojf.pingan.task.Task;
import com.zhaojf.pingan.vo.TokenInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pingan")
public class PinganController {

    @PostMapping("/token")
    public JSONObject setToken(@RequestBody TokenInfo tokenInfo){

        Task.sessionId = tokenInfo.getSessionId();
        Task.signature = tokenInfo.getSignature();
        JSONObject result = new JSONObject();
        result.put("sessionId",Task.sessionId);
        result.put("signature",Task.signature);
        return result;
    }

    @GetMapping("/token")
    public JSONObject getToken(){
        JSONObject result = new JSONObject();
        result.put("sessionId",Task.sessionId);
        result.put("signature",Task.signature);
        return result;
    }

}
