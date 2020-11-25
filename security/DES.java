package security;

import java.util.BitSet;

public class DES {
    private static final int[] ipbox = {58, 50, 42, 34, 26, 18, 10, 2,
                                        60, 52, 44, 36, 28, 20, 12, 4,
                                        62, 54, 46, 38, 30, 22, 14, 6,
                                        64, 56, 48, 40, 32, 24, 16, 8,
                                        57, 49, 41, 33, 25, 17, 9, 1,
                                        59, 51, 43, 35, 27, 19, 11, 3,
                                        61, 53, 45, 37, 29, 21, 13, 5,
                                        63, 55, 47, 39, 31, 23, 15, 7};
    private static final int[] inverseipbox = {40, 8, 48, 16, 56, 24, 64, 32,
                                                39, 7, 47, 15, 55, 23, 63, 31,
                                                38, 6, 46, 14, 54, 22, 62, 30,
                                                37, 5, 45, 13, 53, 21, 61, 29,
                                                36, 4, 44, 12, 52, 20, 60, 28,
                                                35, 3, 43, 11, 51, 19, 59, 27,
                                                34, 2, 42, 10, 50, 18, 58, 26,
                                                33, 1, 41, 9, 49, 17, 57, 25};
    private static final int[] pbox = {16, 7, 20, 21, 29, 12, 28, 17,
                                        1, 15, 23, 26, 5, 18, 31, 10,
                                        2, 8, 24, 14, 32, 27, 3, 9,
                                        19, 13, 30, 6, 22, 11, 4, 25};
    //TODO: the rest of these sboxes
    private static final int[][] sbox = {};
    private static final int[] ebox = {32, 1, 2, 3, 4, 5,
                                        4, 5, 6, 7, 8, 9,
                                        8, 9, 10, 11, 12, 13,
                                        12, 13, 14, 15, 16, 17,
                                        16, 17, 18, 19, 20, 21,
                                        20, 21, 22, 23, 24, 25,
                                        24, 25, 26, 27, 28, 29,
                                        28, 29, 30, 31, 32, 1};
    private static final int[] pc1boxleft = {57, 49, 41, 33, 25, 17,
                                                1, 58, 50, 42, 34, 26, 18,
                                                10, 2, 59, 51, 43, 35, 27,
                                                19, 11, 3, 60, 52, 44, 36};
    private static final int[] pc1boxright = {63, 55, 47, 38, 31, 23, 15,
                                                7, 62, 54, 46, 38, 30, 22,
                                                14, 6, 61, 53, 45, 37, 29,
                                                21, 13, 5, 28, 20, 12, 4};
    private static final int[] pc2box = {14, 17, 11, 24, 1, 5,
                                            3, 28, 15, 6, 21, 10,
                                            23, 19, 12, 4, 26, 8,
                                            16, 7, 27, 20, 13, 2,
                                            41, 52, 31, 37, 47, 55,
                                            30, 40, 51, 45, 33, 48,
                                            44, 49, 39, 56, 34, 53,
                                            46, 42, 50, 36, 29, 32};
    private static final int[] numShifts = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    private final BitSet key;
    private final BitSet[] subKeys;
    private int iteration;

    public DES() {
        this.iteration = 0;
        this.key = generateKey();
        this.subKeys = generateSubkeys(this.key);
    }

    //TODO: write encode method
    public BitSet encrypt(String encryptText) {
        BitSet[] blockStrings = stringTo64Bits(encryptText);
        return new BitSet();
    }

    // for triple DES, needs to be able to encrypt output
    public BitSet encrypt(BitSet bits) {
        return new BitSet();
    }

    //TODO: write decrypt method
    public String decrypt(BitSet decryptText, BitSet key) {
        return "";
    }

    /**
     * Generates the key for DES using random digits
     * @return 64-bit binary key
     */
    private static BitSet generateKey() {
        BitSet key = new BitSet();
        for(int i = 0; i < 64; i++) {
            if(Math.random() < 0.5) {
                key.set(i);
            }
        }
        return key;
    }


    /**
     * Each round of DES is run using this method, increments iteration.
     * @param bits a BitSet where the left-most 32 bits represent the left value, and the right-most 32 bits
     *             represent the right value.
     * @return a BitSet where the left and right are
     */
    private BitSet round(BitSet bits){
        //initially, left side is the first 32 bits
        BitSet initialLeft = new BitSet();
        bits.stream().filter(a -> a < 32).forEach(initialLeft::set);

        //right side is the last 32 bits
        BitSet initialRight = new BitSet();
        bits.stream().filter(a -> a >= 32).forEach(a -> initialRight.set(a - 32));

        //Create new BitSet to store the value of f function of initialRight, xor-ed with initialLeft
        BitSet newRight = this.f(initialRight);
        newRight.xor(initialLeft);

        //finalValue's first 32 bits are the initialRight value
        //Set the last 32 bits of finalValue to the values of newRight
        newRight.stream().forEach(a -> initialRight.set(a + 32));

        this.iteration++;
        return initialRight;
    }

    /**
     * When decoding, run this function to reverse what the method round() does; decrements iteration when it is finished
     * @param bits
     * @return
     */
    private BitSet reverseRound(BitSet bits) {
        //The first 32-bits of an output block are right-most 32 bits of the original input block
        BitSet initialRight = new BitSet();
        bits.stream().filter(a -> a < 32).forEach(initialRight::set);

        //This stores the right-most 32 bits of the argument "bits"
        BitSet decodeRight = new BitSet();
        bits.stream().filter(a -> a >= 32).forEach(a -> decodeRight.set(a - 32));

        //finalValue is first set to the value of the initialRight after it goes through the f-function; after xor-ing this value with the right-most 32 bits of the argument, it should produce the initial left value
        BitSet finalValue = this.f(initialRight);
        finalValue.xor(decodeRight);
        //the first 32 bits of finalValue are currently set to the initialLeft value, and this stream attaches that last 32 bits
        initialRight.stream().forEach(a -> finalValue.set(a + 32));

        this.iteration--;
        return finalValue;
    }

    /**
     * Creates a new BitSet from the permutation table provided, by rearranging the values of the input such that
     *
     * @param bits bits to be converted
     * @param box permutation table
     * @return
     */
    private BitSet permutation(BitSet bits, int[] box) {
        BitSet permutedBits = new BitSet();
        for(int position = 0; position < box.length; position++) {
            permutedBits.set(position, bits.get(box[position]));
        }
        return permutedBits;
    }

    /**
     * Reverses the permutation by using a permutation table and going in the reverse direction, such that
     * bits in the new BitSet are sent back to their original location
     */
    private BitSet reversePermutation(BitSet bits, int[] box) {
        BitSet permutedBits = new BitSet();
        for(int position = 0; position < box.length; position++) {
            permutedBits.set(box[position], bits.get(position));
        }
        return permutedBits;
    }

    /**
     * f function in the DES algorithm, which applies a 48-bit key to the right 32 bits to produce a 32 bit output
     */
    private BitSet f(BitSet input) {
        BitSet bits = permutation(input, ebox);
        bits.xor(subKeys[iteration]);
        return bits;
    }

    /**
     * Performs f function in reverse order for decoding
     */
    private BitSet reverseF(BitSet input) {
        BitSet bits = reverseSBoxTransform(input, sbox[this.iteration]);
        bits.xor(subKeys[this.iteration]);
        return bits;
    }

    //Deepa
    private BitSet sboxTransform(BitSet bits, int[] sbox) {
        return new BitSet();
    }
    //Deepa
    private BitSet reverseSBoxTransform(BitSet bits, int[] sbox) {
        return new BitSet();
    }

    //Deepa
    private BitSet[] generateSubkeys(BitSet bits){
        //do the 16 rounds and change value of this.subKeys;
        return new BitSet[1];
    }

    //Deepa
    private BitSet leftCircularShift (BitSet bits,  int roundnum) {
        //can pull the amount of shifts to make from this.numShifts
        return new BitSet();
    }

    /**
     * Takes input string and breaks it into blocks of 64-bits, stored in a String array where each entry represents 64-bits
     * @param inputString
     * @return String array with each entry having 8 characters
     */
    private BitSet[] stringTo64Bits(String inputString) {
        int length = (inputString.length() / 8) + 1;
        BitSet[] output = new BitSet[length];
        for(int i = 0; i < output.length; i++) {
            output[i] = this.stringToBitString(inputString.substring(i * 8, (i + 1) * 8));
        }
        return output;
    }

    /**
     * Converts inputString into a BitSet by taking each character, changing it to its ASCII value, and then adding
     * leading zeroes so that each character is represented as an 8-bit binary number
     * @param inputString
     * @return a String as represented in binary via ASCII values of each character (64 digits long)
     */
    private BitSet stringToBitString(String inputString) {
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
        for(int i = 0; i < sbr.length(); i++) {
            if(sbr.toString().charAt(i) == '1') {
                bitset.set(i);
            }
        }
        return bitset;
    }
}