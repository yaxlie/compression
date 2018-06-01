package com.company;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class SimpleCompression implements Compression{
    public static int CODING_LENGTH = 6;

    protected String title="file";
    protected String outputName="file";
    protected List<Symbol> symbols = new ArrayList<Symbol>();
    protected HashMap<Character, Symbol> _symbols = new HashMap<Character, Symbol>();
    protected HashMap<String, Symbol> _symbols_code = new HashMap<String, Symbol>();
    protected String text = "";
    protected BitSet bitSet;

    public String loadString(String filename, String outputName){
        try {
            title = filename;
            this.outputName = outputName;
            text = new String(Files.readAllBytes(Paths.get(".\\res\\"+filename))).toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadProbabilities();
        return text;
    }

    public void loadProbabilities(){
        HashMap<Character, Double> _probabilities = new HashMap<Character, Double>();;

        for(char c : text.toCharArray()){
            if (_probabilities.get(c) == null)
                _probabilities.put(c, 1d);
            else
                _probabilities.replace(c, _probabilities.get(c) + 1);
        }

        for (Map.Entry<Character, Double> entry : _probabilities.entrySet()) {
            char key = entry.getKey();
            Double value = entry.getValue();

            symbols.add(new Symbol(key, value / text.length()));
        }

        symbols.sort(new Comparator<Symbol>() {
            @Override
            public int compare(Symbol o1, Symbol o2) {
                if (o1.getProbability() < o2.getProbability()) return 1;
                if (o1.getProbability() > o2.getProbability()) return -1;
                return 0;
            }
        });
    }

    public List<Symbol> create(){
        for(int i=0; i<symbols.size(); i++){
            String code = Integer.toBinaryString(i);

            //maksymalna dlugosc = 6, bo mamy 37 znakow
            while(code.length()<CODING_LENGTH)
                code = "0" + code;
            symbols.get(i).setCode(code);

            _symbols.put(symbols.get(i).getName(), symbols.get(i));
            _symbols_code.put(symbols.get(i).getCode(), symbols.get(i));
        }
        return symbols;
    }

    public BitSet encode(){
        bitSet = new BitSet();
        int counter = 0;

        for(char c : text.toCharArray()){
            for(char c2 : _symbols.get(c).getCode().toCharArray()){
                if(c2=='1')
                bitSet.set(counter);
                counter++;
            }
        }
        return bitSet;
    }

    public void save(){
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(".\\res\\" + outputName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(toByteArray(bitSet));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String load(String filename){
        byte[] data = null;
        try {
            data = Files.readAllBytes(Paths.get(".\\res\\"+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }


        BitSet bset = fromByteArray(data);
        text = "";
        StringBuilder bText = new StringBuilder();

        StringBuilder sb = new StringBuilder();

        String code ="";
        for(int i=0; i<bset.length();i++){
            if(i%CODING_LENGTH==0 && i!=0){
                bText.append(_symbols_code.get(sb.toString()).getName());
                sb = new StringBuilder();
            }
            sb.append(bset.get(i)?1:0);
        }
        text = bText.toString();

        return text;
    }

    protected static BitSet fromByteArray(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i=0; i<bytes.length*8; i++) {
            if ((bytes[bytes.length-i/8-1]&(1<<(i%8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }

    protected static byte[] toByteArray(BitSet bits) {
        byte[] bytes = new byte[bits.length()/8+1];
        for (int i=0; i<bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length-i/8-1] |= 1<<(i%8);
            }
        }
        return bytes;
    }

    public double getEfficiency(){
        double h = 0;
        double l = 0;
        for(Symbol s : symbols){
            l += s.getProbability() * s.getCode().length();
            h += s.getProbability() * (Math.log(s.getProbability())/Math.log(2));
        }
        return (-h)/l;
    }

    public void compress(String filename){
        String[] opt = getClass().getName().split("\\.");
        String text = loadString(filename, opt[opt.length-1]).toLowerCase();
        System.out.println(text.substring(0,20));

        System.out.println("Creating...");
        create();

        System.out.println("Encoding...");
        encode();

        System.out.println("Decoding...");

        System.out.println("Saving...");
        save();

        System.out.println("Loading...");
        text = load(outputName);
        System.out.println(text.substring(0,20));
        System.out.println("Efektywność : " + getEfficiency());
    }

}
