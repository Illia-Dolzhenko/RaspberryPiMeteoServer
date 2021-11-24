package com.dolzhik.meteoServer.service;

import com.dolzhik.meteoServer.entity.DataEntry;

import java.io.IOException;

public interface DataProvider {
    DataEntry getData() throws IOException;
}