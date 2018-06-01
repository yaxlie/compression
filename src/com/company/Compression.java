package com.company;
import java.util.*;

public interface Compression {
    public static int CODING_LENGTH = 6;

    String title="file";
    String outputName = "Compressed";
    List<Symbol> symbols = new ArrayList<Symbol>();
    HashMap<Character, Symbol> _symbols = new HashMap<Character, Symbol>();
    HashMap<String, Symbol> _symbols_code = new HashMap<String, Symbol>();
    String text = "";
    BitSet bitSet = null;

    public String loadString(String filename, String outputName);

    public void loadProbabilities();

    public List<Symbol> create();

    public BitSet encode();

    public void save();

    public String load(String filename);

    public double getEfficiency();

    public void compress(String text);

}
