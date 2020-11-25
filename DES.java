package security;

import java.util.BitSet;

public class DES {
    //static
    //TODO: copy in all of the permutation tables + half s-boxes
    private final int[][] pbox = {};
    private final int[][] sbox = {};
    private BitSet[] subKeys;
    private final int[] numShifts = {};
    private int iteration;

    public DES() {
        this.iteration = 0;
    }

    //TODO: write encode method
    public BitSet encrypt(String encryptText) {
        this.iteration = 0;
        String[] blockStrings = chunk64Bits(encryptText);
        return new BitSet();
    }

    public BitSet encrypt(BitSet bits) {
    }

    //TODO: write decrypt method
    public String decrypt(BitSet decryptText) {
        return "";
    }

    //caroline
    private BitSet generateKey() {

    }

    //caroline
    private BitSet permutation(BitSet bits, int[][] box) {
    }

    //caroline
    private BitSet reversePermutation(BitSet bits, int[][] box) {
    }

    //Deepa
    private BitSet sboxTransform(BitSet bits, int[][] sbox) {
    }

    //Deepa
    private reverseSBoxTransform(BitSet bits, int[][] sbox) {

    }


    //Deepa
    private BitSet leftCircularShift (BitSet bits,  int roundnum){
        //can pull the amount of shifts to make from this.numShifts
    }

    //Deepa
    private BitSet[] generateSubkeys(BitSet bits){
        //do the 16 rounds and change value of this.subKeys;
    }

    //each round of DES (16 rounds)
    private BitSet round(BitSet bits, int roundnum){
    }

    /**
     * Takes input string and breaks it into blocks of 64-bits, stored in a String array where each entry represents 64-bits
     * @param inputString
     * @return String array with each entry having 8 characters
     */
    private String[] stringTo64Bits(String inputString) {
        int length = (inputString.length() / 8) + 1;
        String[] output = new String[length];
        for(int i = 0; i < output.length; i++) {
            output[i] = stringToBitString(inputString.substring(i * 8, (i + 1) * 8));
        }
        return output;
    }

    /**
     * Converts inputString into a char array
     * @param inputString
     * @return a String as represented in binary via ASCII values of each character (64 digits long)
     */
    private String stringToBitString(String inputString) {
        StringBuilder sbr = new StringBuilder();
        for(char c : inputString.toCharArray()) {
            int asciiValue = c;
            String binaryValue = Integer.toBinaryString(asciiValue);
            if(binaryValue.length() < 8) {
                for(int j = 0; j < 8 - binaryValue.length(); j++) {
                    sbr.append("0");
                }
            }
            sbr.append(binaryValue);
        }

        return sbr.toString();
    }


    //caroline
    //TODO: write method
    /**
     * f function, which applies a 48-bit key to the right 32 bits to produce a 32 bit output
     * @return
     */
    private BitSet f(BitSet input) {
        return new BitSet();
    }
}