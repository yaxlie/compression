package com.company;

import java.util.ArrayList;
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
        //text = load(outputName);
        //System.out.println(text.substring(0,20));
        System.out.println("Efektywność : " + getEfficiency());
    }
}
