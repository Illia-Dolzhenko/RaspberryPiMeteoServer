package com.dolzhik.meteoServer.hardware;

import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SysInfo {

    private float cpuVoltage = 0.0f;
    private float cpuTemperature = 0.0f;
    private long memoryFree = 0;
    private long memoryUsed = 0;
    private long memoryTotal = 0;
    private long coreFrequency = 0;
    private long armFrequency = 0;
    private String ipAddress = "";
    private static final Logger logger = LogManager.getLogger(SysInfo.class);

    public SysInfo() {
        try {
            cpuVoltage = SystemInfo.getCpuVoltage();
            cpuTemperature = SystemInfo.getCpuTemperature();
            memoryFree = SystemInfo.getMemoryFree();
            memoryTotal = SystemInfo.getMemoryTotal();
            memoryUsed = SystemInfo.getMemoryUsed();
            coreFrequency = SystemInfo.getClockFrequencyCore();
            armFrequency = SystemInfo.getClockFrequencyArm();
            ipAddress = NetworkInfo.getIPAddress();
        } catch (IOException | InterruptedException e) {
            logger.error("Can't get system info.");
            logger.error(e);
        }
    }

    public float getCpuVoltage() {
        return cpuVoltage;
    }

    public float getCpuTemperature() {
        return cpuTemperature;
    }

    public long getMemoryFree() {
        return memoryFree;
    }

    public long getMemoryUsed() {
        return memoryUsed;
    }

    public long getMemoryTotal() {
        return memoryTotal;
    }

    public long getCoreFrequency() {
        return coreFrequency;
    }

    public long getArmFrequency() {
        return armFrequency;
    }

    public String getIpAddress(){
        return ipAddress;
    }
}
