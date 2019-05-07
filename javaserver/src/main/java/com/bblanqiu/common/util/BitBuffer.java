package com.bblanqiu.common.util;


public class BitBuffer {
	private int currentByte;
	private int currentBit;
	private byte[] byteBuffer;
	private int eofByte;
	private int[] backMask = new int[] {0x0000, 0x0001, 0x0003, 0x0007, 0x000F, 0x001F, 0x003F, 0x007F};
	private int[] frontMask = new int[] {0x0000, 0x0080, 0x00C0, 0x00E0, 0x00F0, 0x00F8, 0x00FC, 0x00FE};
	private boolean eofFlag;
	public BitBuffer(byte[] byteBuffer) {
		this.byteBuffer = byteBuffer;
		this.currentByte = 0;
		this.currentBit = 0;
		this.eofByte = byteBuffer.length;
	}

	public int getBits(int bitsToRead) {
		if (bitsToRead == 0)
			return 0;
		if (eofFlag)
			return -1; // Already at end of file
		int toStore = 0;
		while(bitsToRead != 0  && !eofFlag) {
			if (bitsToRead >= 8 - currentBit) {
				if (currentBit == 0) { // special
					toStore = toStore << 8;
					int cb = ((int) byteBuffer[currentByte]);
					toStore += (cb<0 ? (int) 256 + cb : (int) cb);
					bitsToRead -= 8;
					currentByte++;
				} else {
					toStore = toStore << (8 - currentBit);
					toStore += ((int) byteBuffer[currentByte]) & backMask[8 - currentBit];
					bitsToRead -= (8 - currentBit);
					currentBit = 0;
					currentByte++;
				}
			} else {
				toStore = toStore << bitsToRead;
				int cb = ((int) byteBuffer[currentByte]);
				cb = (cb<0 ? (int) 256 + cb : (int) cb);
				toStore += ((cb) & (0x00FF - frontMask[currentBit])) >> (8 - (currentBit + bitsToRead));
				currentBit += bitsToRead;
				bitsToRead = 0;
			}
			if (currentByte == eofByte) {
				eofFlag = true;
				return toStore;
			}
		}
		return toStore;
	}
	/*int dataLen = 11;
	int dataType = 1;
	int dataLight = 51;
	int dataWetness = 52;
	int dataWater = 53;
	int dataElectricity = 54;
	int dataSense = 1;
	int dataSens1 = 55;
	int dataSens2 = 3;
	int dataSens3 = 16;
	Long dataTime = System.currentTimeMillis();
	
	public static void main(String...args){
		byte[] data = DataConverter.packageData();
		Data d = DataConverter.unwarp(DataConverter.Package(data, 0, data.length));
		BitBuffer b = new BitBuffer(data);
		int type = b.getBits(8);
		int len = b.getBits(8);
		
		int snType = b.getBits(4);
		int sn
		
		int l = b.getBits(8);
		int we = b.getBits(8);
		int wa = b.getBits(8);
		int e = b.getBits(8);
		int s = b.getBits(8);
		int s1 = b.getBits(8);
		int s2 = b.getBits(8);
		int s3 = b.getBits(8);
		int t = b.getBits(8*4);
		System.out.println(t);
		
	}*/
}
