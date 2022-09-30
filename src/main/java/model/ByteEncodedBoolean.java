package model;

public class ByteEncodedBoolean {
    
    public static byte setByteEncodedBoolean(byte byteEncodedBoolean, int bitPosition, boolean setToTrue){
        int mask = (1 << 7-bitPosition);
        if (setToTrue){
            return (byte) (byteEncodedBoolean | mask);
        } else {
            mask = ~ mask;
            return (byte) (byteEncodedBoolean & mask);
        }
    }

    public static boolean getByteEncodedBooleanAt(byte byteEncodedBoolean, int bitPosition){
        int mask = (1 << 7-bitPosition);
        byte result = (byte) (byteEncodedBoolean & mask);
        return result != 0;
    }
}
