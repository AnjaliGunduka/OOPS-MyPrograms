package com.junodx.api.models.core;

public class State {
    private String abbr;

    public State(){

    }

    public State(String s){
        this.abbr = s;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public static boolean isValidStateAbbr(String abbr) {
        if(abbr.length() == 2 && abbr.matches("[A-Z]+"))
            return true;

        return false;
    }
}
