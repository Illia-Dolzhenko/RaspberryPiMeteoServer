package com.dolzhik.meteoServer.provider;

import com.dolzhik.meteoServer.entity.DataEntry;

import java.io.IOException;
import java.util.Map;

public interface DataProvider {
    DataEntry getData() throws IOException;
}