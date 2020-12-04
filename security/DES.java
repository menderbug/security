package security;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;

public class DES {
    private static final int[] ipbox = {58, 50, 42, 34, 26, 18, 10, 2,
							            60, 52, 44, 36, 28, 20, 12, 4,
							            62, 54, 46, 38, 30, 22, 14, 6,
							            64, 56, 48, 40, 32, 24, 16, 8,
							            57, 49, 41, 33, 25, 17, 9,  1,
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
    private static final int[] pbox = {16, 7,  20, 21,
							            29, 12, 28, 17,
							            1,  15, 23, 26,
							            5,  18, 31, 10,
							            2,  8,  24, 14,
							            32, 27, 3,  9,
							            19, 13, 30, 6,
							            22, 11, 4,  25};
    //TODO: the rest of these sboxes
    private final static int[][] sbox = {
      		 {
      		        14, 4,  13, 1,  2,  15, 11, 8,  3,  10, 6,  12, 5,  9,  0,  7,
      		        0,  15, 7,  4,  14, 2,  13, 1,  10, 6,  12, 11, 9,  5,  3,  8,
      		        4,  1,  14, 8,  13, 6,  2,  11, 15, 12, 9,  7,  3,  10, 5,  0,
      		        15, 12, 8,  2,  4,  9,  1,  7,  5,  11, 3,  14, 10, 0,  6,  13
      		    }, {
      		        15, 1,  8,  14, 6,  11, 3,  4,  9,  7,  2,  13, 12, 0,  5,  10,
      		        3,  13, 4,  7,  15, 2,  8,  14, 12, 0,  1,  10, 6,  9,  11, 5,
      		        0,  14, 7,  11, 10, 4,  13, 1,  5,  8,  12, 6,  9,  3,  2,  15,
      		        13, 8,  10, 1,  3,  15, 4,  2,  11, 6,  7,  12, 0,  5,  14, 9
      		    }, {
      		        10, 0,  9,  14, 6,  3,  15, 5,  1,  13, 12, 7,  11, 4,  2,  8,
      		        13, 7,  0,  9,  3,  4,  6,  10, 2,  8,  5,  14, 12, 11, 15, 1,
      		        13, 6,  4,  9,  8,  15, 3,  0,  11, 1,  2,  12, 5,  10, 14, 7,
      		        1,  10, 13, 0,  6,  9,  8,  7,  4,  15, 14, 3,  11, 5,  2,  12
      		    }, {
      		        7,  13, 14, 3,  0,  6,  9,  10, 1,  2,  8,  5,  11, 12, 4,  15,
      		        13, 8,  11, 5,  6,  15, 0,  3,  4,  7,  2,  12, 1,  10, 14, 9,
      		        10, 6,  9,  0,  12, 11, 7,  13, 15, 1,  3,  14, 5,  2,  8,  4,
      		        3,  15, 0,  6,  10, 1,  13, 8,  9,  4,  5,  11, 12, 7,  2,  14
      		    }, {
      		        2,  12, 4,  1,  7,  10, 11, 6,  8,  5,  3,  15, 13, 0,  14, 9,
      		        14, 11, 2,  12, 4,  7,  13, 1,  5,  0,  15, 10, 3,  9,  8,  6,
      		        4,  2,  1,  11, 10, 13, 7,  8,  15, 9,  12, 5,  6,  3,  0,  14,
      		        11, 8,  12, 7,  1,  14, 2,  13, 6,  15, 0,  9,  10, 4,  5,  3
      		    }, {
      		        12, 1,  10, 15, 9,  2,  6,  8,  0,  13, 3,  4,  14, 7,  5,  11,
      		        10, 15, 4,  2,  7,  12, 9,  5,  6,  1,  13, 14, 0,  11, 3,  8,
      		        9,  14, 15, 5,  2,  8,  12, 3,  7,  0,  4,  10, 1,  13, 11, 6,
      		        4,  3,  2,  12, 9,  5,  15, 10, 11, 14, 1,  7,  6,  0,  8,  13
      		    }, {
      		        4,  11, 2,  14, 15, 0,  8,  13, 3,  12, 9,  7,  5,  10, 6,  1,
      		        13, 0,  11, 7,  4,  9,  1,  10, 14, 3,  5,  12, 2,  15, 8,  6,
      		        1,  4,  11, 13, 12, 3,  7,  14, 10, 15, 6,  8,  0,  5,  9,  2,
      		        6,  11, 13, 8,  1,  4,  10, 7,  9,  5,  0,  15, 14, 2,  3,  12
      		    }, {
      		        13, 2,  8,  4,  6,  15, 11, 1,  10, 9,  3,  14, 5,  0,  12, 7,
      		        1,  15, 13, 8,  10, 3,  7,  4,  12, 5,  6,  11, 0,  14, 9,  2,
      		        7,  11, 4,  1,  9,  12, 14, 2,  0,  6,  10, 13, 15, 3,  5,  8,
      		        2,  1,  14, 7,  4,  10, 8,  13, 15, 12, 9,  0,  3,  5,  6,  11
      		    } 
      };
    private static final int[] ebox = {32, 1,  2,  3,  4,  5,
							            4,  5,  6,  7,  8,  9,
							            8,  9,  10, 11, 12, 13,
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
    private static final int[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1,  58, 50, 42, 34, 26, 18,
            10, 2,  59, 51, 43, 35, 27,
            19, 11, 3,  60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7,  62, 54, 46, 38, 30, 22,
            14, 6,  61, 53, 45, 37, 29,
            21, 13, 5,  28, 20, 12, 4
        };    
private static final int[] pc2box = {14, 17, 11, 24, 1,  5,
								        3,  28, 15, 6,  21, 10,
								        23, 19, 12, 4,  26, 8,
								        16, 7,  27, 20, 13, 2,
								        41, 52, 31, 37, 47, 55,
								        30, 40, 51, 45, 33, 48,
								        44, 49, 39, 56, 34, 53,
								        46, 42, 50, 36, 29, 32};
    private static final int[] numShifts = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    private BitSet key;
    private BitSet[] subKeys;

    public DES() {
       this(generateKey());
    }

    public DES(BitSet bitKey) {
        this.key = bitKey;
        this.subKeys = generateSubkeys(this.key);
    }

    public String encrypt(String encryptText) {
        BitSet[] blockStrings = this.stringTo64Bits(encryptText);
        //runs through each set of 64 bits in BlockString
        BitSet encryptedBits = new BitSet();
        String encryptedString = "";
        for(int index = 0; index<blockStrings.length; index++) {
            BitSet currentBits = permutation(blockStrings[index],ipbox); //inital permutation
        	//runs 16 rounds on each 64 bit group
        	for(int iter=0; iter<16; iter++) {
                currentBits = this.round(currentBits, iter);
        	}
        	//does the 32 bit swap
        	BitSet Left = new BitSet();
        	currentBits.stream().filter(a -> a < 32).forEach(Left::set);

            BitSet Right = new BitSet();
            currentBits.stream().filter(a -> a >= 32).forEach(a -> Right.set(a - 32));
            currentBits = this.combineBitSets(Right, Left, 32);

            //inverse permutation
            currentBits = permutation(currentBits, inverseipbox); 
            this.combineBitSets(encryptedBits, currentBits, index * 64);
            encryptedString += bitsetToString(currentBits, 64);
        }
 
        return encryptedString;
       // return encryptedBits;
    }

    public String decrypt(String decryptText, String userkey) {
    	BitSet inputkey = stringToBitSet(userkey);
    	this.key = inputkey;
    	this.subKeys = generateSubkeys(this.key);
    	
    	int length = (decryptText.length()/64);
    	if(decryptText.length()%64 !=0)
    		length++;
        
    	BitSet[] blockStrings = new BitSet[length];
        for(int i = 0; i < blockStrings.length; i++) {
            blockStrings[i] = stringToBitSet(decryptText.substring(i * 64, Math.min(decryptText.length(),(i+1) * 64)));
        }
        BitSet decryptedbits = new BitSet();
         
         //runs through each set of 64 bits in BlockString
         for(int index = 0; index<blockStrings.length; index++) {
        	 int iteration = 15;
        	//inverse permutation
         	BitSet bit = permutation(blockStrings[index],ipbox); 
         	//runs 16 rounds on each 64 bit group
            
         	while(iteration>-1) {   
                 bit = this.reverseRound(bit, iteration);
                 iteration --;
              }
         	//32 bit swap
         	BitSet Left = new BitSet();
            bit.stream().filter(a -> a < 32).forEach(Left::set);

            BitSet Right = new BitSet();
            bit.stream().filter(a -> a >= 32).forEach(a -> Right.set(a - 32));
            bit = this.combineBitSets(Right,Left, 32);
            
         	bit = permutation(bit, inverseipbox); //initial permutation
         	
         	//adds each set of 64 encrypted bits to encryptedbits
         	for(int i = 0; i<64; i++) {
         		decryptedbits.set(i+(index*64), bit.get(i));
         	}
         	decryptedbits = this.combineBitSets(decryptedbits,bit, (index) * 64);
         	
         }
         //converts bits to text and returns text output
    	return this.translateBitsetToText(decryptedbits, blockStrings.length*64);
    }

    /**
     * Generates the key for DES using random digits
     * @return 64-bit binary key
     */
    public static BitSet generateKey() {
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
    private BitSet round(BitSet bits, int iteration){
        //initially, left side is the first 32 bits
        BitSet initialLeft = new BitSet();
        bits.stream().filter(a -> a < 32).forEach(initialLeft::set);

        //right side is the last 32 bits
        BitSet initialRight = new BitSet();
        bits.stream().filter(a -> a >= 32).forEach(a -> initialRight.set(a - 32));
        
        //Create new BitSet to store the value of f function of initialRight, xor-ed with initialLeft
        BitSet newRight = this.f(initialRight, iteration);
        newRight.xor(initialLeft);

        //finalValue's first 32 bits are the initialRight value
        //Set the last 32 bits of finalValue to the values of newRight
        BitSet finalValue = this.combineBitSets(initialRight, newRight, 32);

        return finalValue;
    }

    /**
     * When decoding, run this function to reverse what the method round() does; decrements iteration when it is finished
     * @param bits
     * @return
     */
    private BitSet reverseRound(BitSet bits, int iteration) {
        //The first 32-bits of an output block are right-most 32 bits of the original input block
        BitSet Left = new BitSet();
        bits.stream().filter(a -> a < 32).forEach(Left::set);

        //This stores the right-most 32 bits of the argument "bits"
        BitSet Right = new BitSet();
        bits.stream().filter(a -> a >= 32).forEach(a -> Right.set(a - 32));

        //finalValue is first set to the value of the initialRight after it goes through the f-function; after xor-ing this value with the right-most 32 bits of the argument, it should produce the initial left value
        BitSet finalValue = this.f(Right, iteration);
        finalValue.xor(Left);
        
        finalValue = this.combineBitSets(Right,finalValue, 32);

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
            permutedBits.set(position, bits.get(box[position]-1));
        }
        return permutedBits;
    }

    /**
     * f function in the DES algorithm, which applies a 48-bit key to the right 32 bits to produce a 32 bit output
     */
    private BitSet f(BitSet input, int iteration) {
        BitSet bits = permutation(input, ebox);
        bits.xor(this.subKeys[iteration]);
        bits = sboxTransform(bits,sbox);
        bits = permutation(bits,pbox);


        
        return bits;
    }

    /**
     * Implements sbox on text which takes in 48 bits and reduces it to 32 bits
     */
    private BitSet sboxTransform(BitSet bits, int[][] sbox) {
    	int row = 0;
		int column = 0;
		String binarynum = "";
		String columnbits = "";
		
		for(int index = 0; index<48; index+=6) {
			//gets first and last bit to determine row number in bits
			String rowbits = Integer.toString(bits.get(index) ? 1 : 0) + Integer.toString(bits.get(index+5) ? 1 : 0);
			row = Integer.parseInt(rowbits,2);
			
			//gets middle 4 bits between first and last index to determine column number in bits
			for(int j = index+1; j<index+5; j++) { 
				columnbits += Integer.toString(bits.get(j) ? 1 : 0);
			}
			column = Integer.parseInt(columnbits,2);
			columnbits = "";
			//get the number from the sbox table and converts it to 4 bits. each group of 4 bits is appended to the binarynum. 
			//In the end binarynum is 32 bits 
			int num = sbox[index/6][(row*16)+column];
			binarynum += Integer.toBinaryString(num+ 0b10000).substring(1);
		}
 		//converts 32 bit string (binarynum) to BitSet
		BitSet aftersbox = new BitSet();
		for(int i = 0; i<32; i++) {
			if(binarynum.charAt(i) == '1')
				aftersbox.set(i);
		}
		return aftersbox;
    }

    /**
     * Generates subkeys by taking in a 48 bit key and producing a 48 bit subkey
     */
    private BitSet[] generateSubkeys(BitSet bits){
    	BitSet[] generatedsubKeys = new BitSet[16];
    	bits = permutation(bits, PC1);

    	for(int i = 0; i<16; i++) {
    		//left side is the first 28 bits
            BitSet left = new BitSet();
            bits.stream().filter(a -> a < 28).forEach(left::set);
            
            //right side is the last 28 bits      
            BitSet right = new BitSet();
            bits.stream().filter(a -> a >= 28).forEach(a -> right.set(a - 28));
            
            //depending on iteration value, will run the leftCircularShift method that many times(i.e. iteration = 4, run LeftCircularShift 4 times)
            BitSet rightNew = right;
            for(int roundnum=0; roundnum<i+1; roundnum++) {
                left = leftCircularShift(left, roundnum);
                rightNew = leftCircularShift(rightNew, roundnum);
            }
            //combined the left and right side into one BitSet
            BitSet combined = this.combineBitSets(left,rightNew, 28); // adds the right side bits to BitSet combined starting at index 28

            //run permutation on the 48 bits and store it as a subkey, where iteration number is the index
            generatedsubKeys[i] = this.permutation(combined, pc2box);
    	}
    	return generatedsubKeys;
    }

    /**
     * Shifts bits to the left x spaces where x is defined by the numShifts array
     */
    private static BitSet leftCircularShift (BitSet bits,  int roundnum) {
    	int shift = numShifts[roundnum];
		BitSet temp = bits.get(shift, 28); 
		int j = 0;
    	for(int i = shift; i>0; i--) {
        	temp.set(28-i, bits.get(j)); 
        	j++;
    	}
		return temp;
    }

    /**
     * Takes input string and breaks it into blocks of 64-bits, stored in a BitSet array where each entry represents 64-bits
     * @param inputString
     * @return String array with each entry having 8 characters
     */
    private BitSet[] stringTo64Bits(String inputString) {
        int length = (inputString.length() / 8) ;
        if(inputString.length()%8 !=0)
    		length++;
        BitSet[] output = new BitSet[length];
        for(int i = 0; i < output.length; i++) {
            output[i] = this.translateStringToBitSet(inputString.substring(i * 8, Math.min(inputString.length(), (i + 1) * 8)));
        }
        return output;
    }

    /**
     * Converts inputString into a BitSet by taking each character, changing it to its ASCII value, and then adding
     * leading zeroes so that each character is represented as an 8-bit binary number
     * @param inputString
     * @return a String as represented in binary via ASCII values of each character (64 digits long)
     */
    private BitSet translateStringToBitSet(String inputString) {
        BitSet bitset = new BitSet();
        StringBuilder sbr = new StringBuilder();
        for (char c : inputString.toCharArray()) {
            String binaryValue = Integer.toBinaryString(c);
            //add leading zeroes to numbers that have fewer than 8 bits
            if (binaryValue.length() < 8) {
                for(int i = 0; i < (8 - binaryValue.length()); i++) {
                    sbr.append('0');
                }
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

    public String keyToNums() {
        String binaryString = bitsetToString(this.key, 64);
        StringBuilder sbr = new StringBuilder();
        for(int i = 0; i < 8; i++) {
            sbr.append(Integer.parseInt(binaryString.substring(i * 8, (i+1) * 8), 2) + " ");
        }
        return sbr.toString();
    }

    /**
     * Converts a BitSet back into a String of alphabetical values (must be ASCII encoded)
     * Precondition: bitset is a 64 bit BitSet
     * @param bitset
     * @return
     */
    private String translateBitsetToText(BitSet bitset, int blockSize) {
        String binary = bitsetToString(bitset, blockSize);
        int index = 0;
        StringBuilder overallString = new StringBuilder();
        while(index < binary.length()) {
            overallString.append((char) Integer.parseInt(binary.substring(index, index += 8), 2));
        }
        return overallString.toString();
    }

    public static String bitsetToString(BitSet bitset, int size) {
        StringBuilder sbr = new StringBuilder();
        for(int i = 0; i < size; i++) {
            sbr.append('0');
        }
        bitset.stream().forEach(a -> sbr.setCharAt(a, '1'));
        return sbr.toString();
    }

    private BitSet combineBitSets(BitSet bitsetLeft, BitSet bitsetRight, int offset) {
        bitsetRight.stream().forEach(a -> bitsetLeft.set(a + offset));
        return bitsetLeft;
	}
    
    
    static BitSet stringToBitSet(String str) {
        BitSet bits = new BitSet();
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == '1')
                bits.set(i);
        }
        return bits;
    }

    public BitSet getKey() {
    	return this.key;
    }
}