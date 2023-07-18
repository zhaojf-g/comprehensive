package com.zhaojf.pingan.controller;

import com.zhaojf.pingan.entity.AppointmentRecord;
import com.zhaojf.pingan.entity.User;
import com.zhaojf.pingan.service.AppointmentRecordService;
import com.zhaojf.pingan.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pingan")
public class PinganController {

    private final UserService userService;

    private final AppointmentRecordService appointmentRecordService;

    public PinganController(UserService userService, AppointmentRecordService appointmentRecordService) {
        this.userService = userService;
        this.appointmentRecordService = appointmentRecordService;
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
    public List<User> getToken() {

        return userService.list();
    }

    @PostMapping("/record")
    public void addRecord(@RequestBody AppointmentRecord record) {

        appointmentRecordService.save(record);

    }

}
