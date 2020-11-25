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


    //TODO: write encode method
    /*public BitSet encrypt(String encryptText) {
        this.iteration = 0;
        String[] blockStrings = stringTo64Bits(encryptText);
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

    //Caroline
    //each round of DES (16 rounds)
    private BitSet round(BitSet bits, int roundnum){
    }
    */

    /**
     * Takes input string and breaks it into blocks of 64-bits, stored in a String array where each entry represents 64-bits
     * @param inputString
     * @return String array with each entry having 8 characters
     */
    private BitSet[] stringTo64Bits(String inputString) {
        int length = (inputString.length() / 8) + 1;
        BitSet[] output = new BitSet[length];
        for(int i = 0; i < output.length; i++) {
            output[i] = stringToBitString(inputString.substring(i * 8, (i + 1) * 8));
        }
        return output;
    }

    /**
     * Converts inputString into a BitSet by taking each character, changing it to its ASCII value, and then adding
     * leading zeroes so that each character is represented as an 8-bit binary number
     * @param inputString
     * @return a String as represented in binary via ASCII values of each character (64 digits long)
     */
    private static BitSet stringToBitString(String inputString) {
        BitSet bitset = new BitSet();
        StringBuilder sbr = new StringBuilder();
        for (char c : inputString.toCharArray()) {
            String binaryValue = Integer.toBinaryString(c);
            //add leading zeroes to numbers that have fewer than 8 bits
            if (binaryValue.length() < 8) {
                sbr.append("0".repeat(8 - binaryValue.length()));
            }
            sbr.append(binaryValue);
        }
        System.out.println(sbr.toString());
        for(int i = 0; i < sbr.length(); i++) {
            if(sbr.toString().charAt(i) == '1') {
                bitset.set(i);
            }
        }
        return bitset;
    }

    public static void main(String[] args) {
        System.out.println(stringToBitString("hello").toString());
    }

    //caroline
    /**
     * f function, which applies a 48-bit key to the right 32 bits to produce a 32 bit output
     * @return
     */
    private BitSet f(BitSet input) {
        return new BitSet();
    }
}