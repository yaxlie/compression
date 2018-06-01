package com.company;

public class Main {

    public static void main(String[] args) {
        SimpleCompression compression = new SimpleCompression();
        HuffmanCompression huffmanCompression = new HuffmanCompression();

        System.out.println("\n\n -----Simple compression----- \n\n");
        compression.compress("norm_wiki_sample.txt");

        System.out.println("\n\n -----Huffman compression----- \n\n");
        huffmanCompression.compress("norm_wiki_sample.txt");
    }
}
