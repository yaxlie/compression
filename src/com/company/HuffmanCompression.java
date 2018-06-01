package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;


public class HuffmanCompression extends SimpleCompression{
    List<Node>nodes = new ArrayList<Node>();

    @Override
    public List<Symbol> create(){

        //build a tree
        for(int i=symbols.size()-1; i>=0; i--){
            Node node = new Node();
            node.setSymbol(symbols.get(i));
            node.setValue(symbols.get(i).getProbability());
            nodes.add(node);
        }

        while(nodes.size()>1){
            Node nodeParent = new Node();
            nodeParent.setValue(nodes.get(0).getValue() + nodes.get(1).getValue());
            nodeParent.setLeftChild(nodes.get(0));
            nodeParent.setRightChild(nodes.get(1));
            nodes.remove(1);
            nodes.remove(0);
            nodes.add(nodeParent);
            sortNodes();
        }

        for(Symbol s : symbols){
            s = searchTreeAndGetCode(s, "", nodes.get(0));
            _symbols.put(s.getName(), s);
            _symbols_code.put(s.getCode(), s);
//            System.out.println(s.getCode());
        }

        return symbols;
    }

    private void sortNodes(){
        nodes.sort(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getValue() < o2.getValue()) return -1;
                if (o1.getValue() > o2.getValue()) return 1;
                return 0;
            }
        });
    }

    private Symbol searchTreeAndGetCode(Symbol s, String code, Node node){
        if(node.getSymbol() == s) {
            s.setCode(code);
            return s;
        }
        if(node.getLeftChild() != null)
            s = searchTreeAndGetCode(s,code + "0", node.getLeftChild());
        if(node.getRightChild() != null && s.getCode() == null)
            s = searchTreeAndGetCode(s,code + "1", node.getRightChild());
        return s;
    }

    @Override
    public String load(String filename){
        byte[] data = null;
        try {
            data = Files.readAllBytes(Paths.get(".\\res\\" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitSet bset = fromByteArray(data);
        text = "";

        StringBuilder bText = new StringBuilder();
        StringBuilder sb = new StringBuilder();

        for(int i=0; i<bset.length();i++){
            Symbol s = _symbols_code.get(sb.toString());

            if(s!=null){
                bText.append(s.getName());
                sb = new StringBuilder();
            }
            sb.append(bset.get(i)?1:0);
        }
        text = bText.toString();

        return text;
    }
}
