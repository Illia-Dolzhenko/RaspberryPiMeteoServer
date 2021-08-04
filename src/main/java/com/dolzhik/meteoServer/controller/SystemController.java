package com.dolzhik.meteoServer.controller;

import com.dolzhik.meteoServer.hardware.SysInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemController {

    @GetMapping("/systemInfo")
    public Object getSystemInfo(){
        return new SysInfo();
    }
}
