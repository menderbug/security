import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class DES {
    private final int[][] pbox = {};
    private final int[][] sbox = {};

    //TODO: write encode method
    public BitSet encrypt(String encryptText) {

        return new BitSet();
    }

    //TODO: write decrypt method
    public String decrypt(BitSet decryptText) {
        return "";
    }

    //TODO: write method
    /**
     * Takes input string and breaks it into blocks of 64-bits
     * @param inputString
     * @return
     */
    private static BitSet[] chunk64Bits(String inputString) {
        int length = (inputString.length() / 8) + 1;
        BitSet[] output = new BitSet[length];
        for(int i = 0; i < output.length; i++) {
            output[i] = stringToBits(inputString.substring(i * 8, (i + 1) * 8));
        }
        return output;
    }

    /**
     * Converts inputString into a BitSet
     * @param inputString
     * @return
     */
    private static BitSet stringToBits(String inputString) {
        BitSet outputBits = new BitSet();
        for(char c : inputString.toCharArray()) {

        }
    }

    //TODO: write method
    /**
     * f function, which applies a 48-bit key to the right 32 bits to produce a 32 bit output
     * @return
     */
    private BitSet f(BitSet input) {
        return new BitSet();
    }
}