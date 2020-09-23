package com.example.secretkeeper;


public class Data {

    String key;

    String url;
    String secret;

    public Data(String key, String url, String secret) {
        this.key = key;
        this.url = url;

        this.secret = secret;
    }

    public Data() {
    }



    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }



    public String getSecret() {
        return secret;
    }
}