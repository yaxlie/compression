package com.company;

public class Symbol {
    private char name;
    private double probability;
    private String code;

    public Symbol(char name, double probability){
        this.name = name;
        this.probability = probability;
    }

    public char getName() {
        return name;
    }

    public void setName(char name) {
        this.name = name;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
