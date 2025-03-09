package com.globant.data;

import org.testng.annotations.DataProvider;

public class DataProviders {
    @DataProvider(name = "userData")
    public Object[][] userData() {
        return new Object[][]{
                {"DoeDoe01", "John", "Doe", "johndoe@mail.com", "securepass1234", "55551239"},
                {"DoeDoe02", "Jane", "Doe", "jane.doe@mail.com", "anotherpass5678", "55559877"}
        };
    }
}
