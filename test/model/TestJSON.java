package test.model;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJSON{
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Jackson is working!");
    }
}
