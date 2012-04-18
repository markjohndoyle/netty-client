package org.hbird.camel.nettyclient;

/**
 * Support class providing useful test data.
 * @author Mark Doyle
 *
 */
public class DataGenerator {

	public static final byte BYTE = 1;
	public static final int INT_ONE = 255;
	public static final int INT_TWO = 90;
	public static final int BYTE_INT_MASK = 0xff;

	//	private DataGenerator() {
	//		// Utility class, no instantiation please!
	//	}

	public static byte[] createRawTestData() {
		final byte[] bytes = new byte[9];

		bytes[0] = DataGenerator.BYTE;
		bytes[1] = (byte) DataGenerator.INT_ONE;
		bytes[2] = (byte) (DataGenerator.INT_ONE >> 8);
		bytes[3] = (byte) (DataGenerator.INT_ONE >> 16);
		bytes[4] = (byte) (DataGenerator.INT_ONE >> 24);
		bytes[5] = (byte) DataGenerator.INT_TWO;
		bytes[6] = (byte) (DataGenerator.INT_TWO >> 8);
		bytes[7] = (byte) (DataGenerator.INT_TWO >> 16);
		bytes[8] = (byte) (DataGenerator.INT_TWO >> 24);

		return bytes;
	}

}
