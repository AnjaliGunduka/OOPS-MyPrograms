package com.junodx.api.models.core;

public class ZipCode {
    private String zip;

    public ZipCode(){

    }

    public ZipCode(String s){
        this.zip = s;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public static boolean isValidZipCode(String zip) {
        //Either 90210 format or 90210-2231
        if(zip.length() == 5 && zip.matches("[0-9]+"))
            return true;
        else if(zip.length() == 10){
            String[] contents = zip.split("-");
            if(contents.length == 2) {
                String dest = contents[0];
                String route = contents[1];
                if(dest.length() == 5 && dest.matches("[0-9]+") && route.length() == 4 && route.matches("[0-9]+"))
                    return true;
            }
        }

        return false;
    }
}
