package com.dolzhik.meteoServer.service;

import com.dolzhik.meteoServer.entity.DataEntry;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@Service
public class BMProvider implements DataProvider {
    // Get I2C device, BME280 I2C address is 0x76(108)
    private I2CDevice device;

    private static final Logger LOG = LogManager.getLogger(BMProvider.class);

    public BMProvider() {
        I2CBus bus;
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
            device = bus.getDevice(0x76);
        } catch (I2CFactory.UnsupportedBusNumberException | IOException e) {
            LOG.error("Can't connect to BM sensor.");
            e.printStackTrace();
            bus = null;
            device = null;
        }
    }

    @Override
    public DataEntry getData() throws IOException {
        DataEntry dataEntry = new DataEntry(0.0,0.0,0.0,0.0, new Timestamp(Instant.now().toEpochMilli()));

        if(device != null) {
            byte[] b1 = new byte[24];
            device.read(0x88, b1, 0, 24);

            // Convert the data
            // temp coefficients
            int dig_T1 = (b1[0] & 0xFF) + ((b1[1] & 0xFF) * 256);
            int dig_T2 = (b1[2] & 0xFF) + ((b1[3] & 0xFF) * 256);
            if (dig_T2 > 32767) {
                dig_T2 -= 65536;
            }
            int dig_T3 = (b1[4] & 0xFF) + ((b1[5] & 0xFF) * 256);
            if (dig_T3 > 32767) {
                dig_T3 -= 65536;
            }

            // pressure coefficients
            int dig_P1 = (b1[6] & 0xFF) + ((b1[7] & 0xFF) * 256);
            int dig_P2 = (b1[8] & 0xFF) + ((b1[9] & 0xFF) * 256);
            if (dig_P2 > 32767) {
                dig_P2 -= 65536;
            }
            int dig_P3 = (b1[10] & 0xFF) + ((b1[11] & 0xFF) * 256);
            if (dig_P3 > 32767) {
                dig_P3 -= 65536;
            }
            int dig_P4 = (b1[12] & 0xFF) + ((b1[13] & 0xFF) * 256);
            if (dig_P4 > 32767) {
                dig_P4 -= 65536;
            }
            int dig_P5 = (b1[14] & 0xFF) + ((b1[15] & 0xFF) * 256);
            if (dig_P5 > 32767) {
                dig_P5 -= 65536;
            }
            int dig_P6 = (b1[16] & 0xFF) + ((b1[17] & 0xFF) * 256);
            if (dig_P6 > 32767) {
                dig_P6 -= 65536;
            }
            int dig_P7 = (b1[18] & 0xFF) + ((b1[19] & 0xFF) * 256);
            if (dig_P7 > 32767) {
                dig_P7 -= 65536;
            }
            int dig_P8 = (b1[20] & 0xFF) + ((b1[21] & 0xFF) * 256);
            if (dig_P8 > 32767) {
                dig_P8 -= 65536;
            }
            int dig_P9 = (b1[22] & 0xFF) + ((b1[23] & 0xFF) * 256);
            if (dig_P9 > 32767) {
                dig_P9 -= 65536;
            }

            // Read 1 byte of data from address 0xA1(161)
            int dig_H1 = ((byte) device.read(0xA1) & 0xFF);

            // Read 7 bytes of data from address 0xE1(225)
            device.read(0xE1, b1, 0, 7);

            // Convert the data
            // humidity coefficients
            int dig_H2 = (b1[0] & 0xFF) + (b1[1] * 256);
            if (dig_H2 > 32767) {
                dig_H2 -= 65536;
            }
            int dig_H3 = b1[2] & 0xFF;
            int dig_H4 = ((b1[3] & 0xFF) * 16) + (b1[4] & 0xF);
            if (dig_H4 > 32767) {
                dig_H4 -= 65536;
            }
            int dig_H5 = ((b1[4] & 0xFF) / 16) + ((b1[5] & 0xFF) * 16);
            if (dig_H5 > 32767) {
                dig_H5 -= 65536;
            }
            int dig_H6 = b1[6] & 0xFF;
            if (dig_H6 > 127) {
                dig_H6 -= 256;
            }

            // Select control humidity register
            // Humidity over sampling rate = 1
            device.write(0xF2, (byte) 0x01);
            // Select control measurement register
            // Normal mode, temp and pressure over sampling rate = 1
            device.write(0xF4, (byte) 0x27);
            // Select config register
            // Stand_by time = 1000 ms
            device.write(0xF5, (byte) 0xA0);

            // Read 8 bytes of data from address 0xF7(247)
            // pressure msb1, pressure msb, pressure lsb, temp msb1, temp msb, temp lsb, humidity lsb, humidity msb
            byte[] data = new byte[8];
            device.read(0xF7, data, 0, 8);

            // Convert pressure and temperature data to 19-bits
            long adc_p = (((long) (data[0] & 0xFF) * 65536) + ((long) (data[1] & 0xFF) * 256) + (long) (data[2] & 0xF0)) / 16;
            long adc_t = (((long) (data[3] & 0xFF) * 65536) + ((long) (data[4] & 0xFF) * 256) + (long) (data[5] & 0xF0)) / 16;
            // Convert the humidity data
            long adc_h = ((long) (data[6] & 0xFF) * 256 + (long) (data[7] & 0xFF));

            // Temperature offset calculations
            double var1 = (((double) adc_t) / 16384.0 - ((double) dig_T1) / 1024.0) * ((double) dig_T2);
            double var2 = ((((double) adc_t) / 131072.0 - ((double) dig_T1) / 8192.0) *
                    (((double) adc_t) / 131072.0 - ((double) dig_T1) / 8192.0)) * ((double) dig_T3);
            double t_fine = (long) (var1 + var2);
            double cTemp = (var1 + var2) / 5120.0;

            // Pressure offset calculations
            var1 = ((double) t_fine / 2.0) - 64000.0;
            var2 = var1 * var1 * ((double) dig_P6) / 32768.0;
            var2 = var2 + var1 * ((double) dig_P5) * 2.0;
            var2 = (var2 / 4.0) + (((double) dig_P4) * 65536.0);
            var1 = (((double) dig_P3) * var1 * var1 / 524288.0 + ((double) dig_P2) * var1) / 524288.0;
            var1 = (1.0 + var1 / 32768.0) * ((double) dig_P1);
            double p = 1048576.0 - (double) adc_p;
            p = (p - (var2 / 4096.0)) * 6250.0 / var1;
            var1 = ((double) dig_P9) * p * p / 2147483648.0;
            var2 = p * ((double) dig_P8) / 32768.0;
            double pressure = (p + (var1 + var2 + ((double) dig_P7)) / 16.0) / 100;

            // Output data to screen
            System.out.printf("Temperature in Celsius : %.2f C %n", cTemp);
            System.out.printf("Pressure : %.2f hPa %n", pressure);

            dataEntry.setTemperature(cTemp);
            dataEntry.setPressure(pressure);
        }

        return dataEntry;
    }
}

