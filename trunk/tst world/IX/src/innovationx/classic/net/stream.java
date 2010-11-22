package src.innovationx.classic.net;

public class stream {

    public static int bitMaskOut[] = new int[32];
    public Cryption packetEncryption = null;
    public byte[] buffer;
    public int currentOffset = 0;
    public int bitPosition = 0;
    private int framePos = 0;

    public stream(byte[] abyte0) {
        buffer = abyte0;
    }

    public byte readSignedByteA() {
        return (byte) (buffer[currentOffset++] - 128);
    }

    public byte readSignedByteC() {
        return (byte) (-buffer[currentOffset++]);
    }

    public byte readSignedByteS() {
        return (byte) (128 - buffer[currentOffset++]);
    }

    public int readUnsignedByteA() {
        return buffer[currentOffset++] - 128 & 0xff;
    }

    public int readUnsignedByteC() {
        return -buffer[currentOffset++] & 0xff;
    }

    public int readUnsignedByteS() {
        return 128 - buffer[currentOffset++] & 0xff;
    }

    public int readSignedWordBigEndian() {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
        if (i > 32767) {
            i -= 0x10000;
        }
        return i;
    }

    public int readSignedWordA() {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] - 128 & 0xff);
        if (i > 32767) {
            i -= 0x10000;
        }
        return i;
    }

    public int readSignedWordBigEndianA() {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] - 128 & 0xff);
        if (i > 32767) {
            i -= 0x10000;
        }
        return i;
    }

    public int readUnsignedWordBigEndian() {
        currentOffset += 2;
        return ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
    }

    public int readUnsignedWordA() {
        currentOffset += 2;
        return ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] - 128 & 0xff);
    }

    public int readUnsignedWordBigEndianA() {
        currentOffset += 2;
        return ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] - 128 & 0xff);
    }

    public int readDWord_v1() {
        currentOffset += 4;
        return ((buffer[currentOffset - 2] & 0xff) << 24) + ((buffer[currentOffset - 1] & 0xff) << 16) + ((buffer[currentOffset - 4] & 0xff) << 8) + (buffer[currentOffset - 3] & 0xff);
    }

    public int readDWord_v2() {
        currentOffset += 4;
        return ((buffer[currentOffset - 3] & 0xff) << 24) + ((buffer[currentOffset - 4] & 0xff) << 16) + ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
    }

    public int readUnsignedByte() {
        return buffer[currentOffset++] & 0xff;
    }

    public byte readSignedByte() {
        if (currentOffset >= buffer.length) {
            return 10;
        }
        return buffer[currentOffset++];
    }

    public int readUnsignedWord() {
        currentOffset += 2;
        return ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
    }

    public int readSignedWord() {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
        if (i > 32767) {
            i -= 0x10000;
        }
        return i;
    }

    public int readDWord() {
        currentOffset += 4;
        return ((buffer[currentOffset - 4] & 0xff) << 24) + ((buffer[currentOffset - 3] & 0xff) << 16) + ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
    }

    public long readQWord() {
        long l = (long) readDWord() & 0xffffffffL;
        long l1 = (long) readDWord() & 0xffffffffL;
        return (l << 32) + l1;
    }

    public long readQWord2() {
        currentOffset += 8;
        return (((buffer[currentOffset - 8] & 0xff) << 56) + ((buffer[currentOffset - 7] & 0xff) << 48) + ((buffer[currentOffset - 6] & 0xff) << 40) + ((buffer[currentOffset - 5] & 0xff) << 32) + ((buffer[currentOffset - 4] & 0xff) << 24) + ((buffer[currentOffset - 3] & 0xff) << 16) + ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff));
    }

    public String readString() {
        int i = currentOffset;
        while (readSignedByte() != 10);
        return new String(buffer, i, currentOffset - i - 1);
    }

    public void writeByteA(int i) {
        checkCapacity(1);
        buffer[currentOffset++] = (byte) (i + 128);
    }

    public void writeByteS(int i) {
        checkCapacity(1);
        buffer[currentOffset++] = (byte) (128 - i);
    }

    public void writeByteC(int i) {
        checkCapacity(1);
        buffer[currentOffset++] = (byte) (-i);
    }

    public void writeWordBigEndianA(int i) {
        checkCapacity(2);
        buffer[currentOffset++] = (byte) (i + 128);
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    public void writeWordA(int i) {
        checkCapacity(2);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) (i + 128);
    }

    public void writeWordBigEndian_dup(int i) {
        checkCapacity(2);
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    public void writeDWord_v1(int i) {
        checkCapacity(4);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 24);
        buffer[currentOffset++] = (byte) (i >> 16);
    }

    public void writeDWord_v2(int i) {
        checkCapacity(4);
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 24);
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    public void readBytes_reverse(byte abyte0[], int i, int j) {
        for (int k = (j + i) - 1; k >= j; k--) {
            abyte0[k] = buffer[currentOffset++];

        }
    }

    public void writeBytes_reverse(byte abyte0[], int i, int j) {
        for (int k = (j + i) - 1; k >= j; k--) {
            checkCapacity(1);
            buffer[currentOffset++] = abyte0[k];
        }
    }

    public void readBytes_reverseA(byte abyte0[], int i, int j) {
        for (int k = (j + i) - 1; k >= j; k--) {
            abyte0[k] = (byte) (buffer[currentOffset++] - 128);
        }
    }

    public void writeBytes_reverseA(byte abyte0[], int i, int j) {
        for (int k = (j + i) - 1; k >= j; k--) {
            checkCapacity(1);
            buffer[currentOffset++] = (byte) (abyte0[k] + 128);
        }
    }

    public void createFrame(int id) {
        checkCapacity(1);
        buffer[currentOffset++] = (byte) (id + packetEncryption.getNextKey());
    }

    public void createFrameVarSize(int id) {
        checkCapacity(2);
        buffer[currentOffset++] = (byte) (id + packetEncryption.getNextKey());
        writeByte(0);
        framePos = currentOffset - 1;
    }

    public void createFrameVarSizeWord(int id) {
        checkCapacity(3);
        buffer[currentOffset++] = (byte) (id + packetEncryption.getNextKey());
        writeWord(0);
        framePos = currentOffset - 2;
    }

    public void endFrameVarSize() {
        writeByte(currentOffset - (framePos + 2) + 1, framePos);
    }

    public void endFrameVarSizeWord() {
        int size = currentOffset - (framePos + 2);
        writeByte(size >> 8, framePos++);
        writeByte(size, framePos);
    }

    public void writeByte(int i) {
        checkCapacity(1);
        buffer[currentOffset++] = (byte) i;
    }

    public void writeByte(int i, int position) {
        buffer[position] = (byte) i;
    }

    public void writeWord(int i) {
        checkCapacity(2);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    public void writeWordBigEndian(int i) {
        checkCapacity(2);
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    public void write3Byte(int i) {
        checkCapacity(3);
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    public void writeDWord(int i) {
        checkCapacity(4);
        buffer[currentOffset++] = (byte) (i >> 24);
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    public void writeDWordBigEndian(int i) {
        checkCapacity(4);
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 24);
    }

    public void writeQWord(long l) {
        checkCapacity(8);
        buffer[currentOffset++] = (byte) (int) (l >> 56);
        buffer[currentOffset++] = (byte) (int) (l >> 48);
        buffer[currentOffset++] = (byte) (int) (l >> 40);
        buffer[currentOffset++] = (byte) (int) (l >> 32);
        buffer[currentOffset++] = (byte) (int) (l >> 24);
        buffer[currentOffset++] = (byte) (int) (l >> 16);
        buffer[currentOffset++] = (byte) (int) (l >> 8);
        buffer[currentOffset++] = (byte) (int) l;
    }

    public void writeString(String s) {
        checkCapacity(s.length() + 5);
        System.arraycopy(s.getBytes(), 0, buffer, currentOffset, s.length());
        currentOffset += s.length();
        buffer[currentOffset++] = 10;
    }

    public void writeBytes(byte abyte0[], int i, int j) {
        for (int k = j; k < j + i; k++) {
            checkCapacity(1);
            buffer[currentOffset++] = abyte0[k];
        }
    }

    public void readBytes(byte abyte0[], int i, int j) {
        for (int k = j; k < j + i; k++) {
            abyte0[k] = buffer[currentOffset++];
        }
    }

    public void initBitAccess() {
        bitPosition = currentOffset * 8;
    }

    public void writeBits(int numBits, int value) {
        int bytePos = bitPosition >> 3;
        int bitOffset = 8 - (bitPosition & 7);
        bitPosition += numBits;
        for (; numBits > bitOffset; bitOffset = 8) {
            checkPosition(bytePos);
            buffer[bytePos] &= ~bitMaskOut[bitOffset];
            buffer[bytePos++] |= (value >> (numBits - bitOffset)) & bitMaskOut[bitOffset];
            numBits -= bitOffset;
        }
        if (numBits == bitOffset) {
            checkPosition(bytePos);
            buffer[bytePos] &= ~bitMaskOut[bitOffset];
            buffer[bytePos] |= value & bitMaskOut[bitOffset];
        } else {
            checkPosition(bytePos);
            buffer[bytePos] &= ~(bitMaskOut[numBits] << (bitOffset - numBits));
            buffer[bytePos] |= (value & bitMaskOut[numBits]) << (bitOffset - numBits);
        }
    }

    public void finishBitAccess() {
        currentOffset = (bitPosition + 7) / 8;
    }

    public void checkCapacity(int length) {
        if ((currentOffset + length + 4) >= buffer.length) {
            byte[] tempBuffer = buffer;
            buffer = new byte[(currentOffset + length + 4) * 2];
            System.arraycopy(tempBuffer, 0, buffer, 0, currentOffset);
        }
    }

    public void checkPosition(int pos) {
        if (pos >= buffer.length) {
            byte[] tempBuffer = buffer;
            buffer = new byte[pos * 2];
            System.arraycopy(tempBuffer, 0, buffer, 0, currentOffset);
        }
    }

    static {
        for (int i = 0; i < 32; i++) {
            bitMaskOut[i] = (1 << i) - 1;
        }
    }
}
