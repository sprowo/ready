package com.prowo.persist.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class UUID implements java.io.Serializable, Comparable<UUID> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1568435898065532412L;

	/*
	 * The most significant 64 bits of this UUID.
	 * 
	 * @serial
	 */
	private final long mostSigBits;

	/*
	 * The least significant 64 bits of this UUID.
	 * 
	 * @serial
	 */
	private final long leastSigBits;

	/*
	 * The version number associated with this UUID. Computed on demand.
	 */
	private transient int version = -1;

	/*
	 * The variant number associated with this UUID. Computed on demand.
	 */
	private transient int variant = -1;

	/*
	 * The timestamp associated with this UUID. Computed on demand.
	 */
	private transient volatile long timestamp = -1;

	/*
	 * The clock sequence associated with this UUID. Computed on demand.
	 */
	private transient int sequence = -1;

	/*
	 * The node number associated with this UUID. Computed on demand.
	 */
	private transient long node = -1;

	/*
	 * The hashcode of this UUID. Computed on demand.
	 */
	private transient int hashCode = -1;

	/*
	 * The random number generator used by this class to create random based UUIDs.
	 */
	private static volatile SecureRandom numberGenerator = null;

	// Constructors and Factories

	/*
	 * Private constructor which uses a byte array to construct the new UUID.
	 */
	private UUID(byte[] data) {
		long msb = 0;
		long lsb = 0;
		assert data.length == 16;
		for (int i = 0; i < 8; i++)
			msb = (msb << 8) | (data[i] & 0xff);
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (data[i] & 0xff);
		this.mostSigBits = msb;
		this.leastSigBits = lsb;
	}

	/**
	 * Constructs a new <tt>UUID</tt> using the specified data. <tt>mostSigBits</tt> is used for the most significant 64
	 * bits of the <tt>UUID</tt> and <tt>leastSigBits</tt> becomes the least significant 64 bits of the <tt>UUID</tt>.
	 * 
	 * @param mostSigBits
	 * @param leastSigBits
	 */
	public UUID(long mostSigBits, long leastSigBits) {
		this.mostSigBits = mostSigBits;
		this.leastSigBits = leastSigBits;
	}

	/**
	 * Static factory to retrieve a type 4 (pseudo randomly generated) UUID.
	 * 
	 * The <code>UUID</code> is generated using a cryptographically strong pseudo random number generator.
	 * 
	 * @return a randomly generated <tt>UUID</tt>.
	 */
	public static UUID randomUUID() {
		SecureRandom ng = numberGenerator;
		if (ng == null) {
			numberGenerator = ng = new SecureRandom();
		}

		byte[] randomBytes = new byte[16];
		ng.nextBytes(randomBytes);
		randomBytes[6] &= 0x0f; /* clear version */
		randomBytes[6] |= 0x40; /* set to version 4 */
		randomBytes[8] &= 0x3f; /* clear variant */
		randomBytes[8] |= 0x80; /* set to IETF variant */
		return new UUID(randomBytes);
	}
	
	public static String uuid(){
		return randomUUID().toString();
	}


	/**
	 * Static factory to retrieve a type 3 (name based) <tt>UUID</tt> based on the specified byte array.
	 * 
	 * @param name
	 *            a byte array to be used to construct a <tt>UUID</tt>.
	 * @return a <tt>UUID</tt> generated from the specified array.
	 */
	public static UUID nameUUIDFromBytes(byte[] name) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsae) {
			throw new InternalError("MD5 not supported");
		}
		byte[] md5Bytes = md.digest(name);
		md5Bytes[6] &= 0x0f; /* clear version */
		md5Bytes[6] |= 0x30; /* set to version 3 */
		md5Bytes[8] &= 0x3f; /* clear variant */
		md5Bytes[8] |= 0x80; /* set to IETF variant */
		return new UUID(md5Bytes);
	}

	/**
	 * Creates a <tt>UUID</tt> from the string standard representation as described in the {@link #toString} method.
	 * 
	 * @param name
	 *            a string that specifies a <tt>UUID</tt>.
	 * @return a <tt>UUID</tt> with the specified value.
	 * @throws IllegalArgumentException
	 *             if name does not conform to the string representation as described in {@link #toString}.
	 */
	public static UUID fromString(String name) {
		String[] components = name.split("-");
		if (components.length != 5)
			throw new IllegalArgumentException("Invalid UUID string: " + name);
		for (int i = 0; i < 5; i++)
			components[i] = "0x" + components[i];

		long mostSigBits = Long.decode(components[0]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[1]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[2]).longValue();

		long leastSigBits = Long.decode(components[3]).longValue();
		leastSigBits <<= 48;
		leastSigBits |= Long.decode(components[4]).longValue();

		return new UUID(mostSigBits, leastSigBits);
	}

	// Field Accessor Methods

	/**
	 * Returns the least significant 64 bits of this UUID's 128 bit value.
	 * 
	 * @return the least significant 64 bits of this UUID's 128 bit value.
	 */
	public long getLeastSignificantBits() {
		return leastSigBits;
	}

	/**
	 * Returns the most significant 64 bits of this UUID's 128 bit value.
	 * 
	 * @return the most significant 64 bits of this UUID's 128 bit value.
	 */
	public long getMostSignificantBits() {
		return mostSigBits;
	}

	/**
	 * The version number associated with this <tt>UUID</tt>. The version number describes how this <tt>UUID</tt> was
	 * generated.
	 * 
	 * The version number has the following meaning:
	 * <p>
	 * <ul>
	 * <li>1 Time-based UUID
	 * <li>2 DCE security UUID
	 * <li>3 Name-based UUID
	 * <li>4 Randomly generated UUID
	 * </ul>
	 * 
	 * @return the version number of this <tt>UUID</tt>.
	 */
	public int version() {
		if (version < 0) {
			// Version is bits masked by 0x000000000000F000 in MS long
			version = (int) ((mostSigBits >> 12) & 0x0f);
		}
		return version;
	}

	/**
	 * The variant number associated with this <tt>UUID</tt>. The variant number describes the layout of the
	 * <tt>UUID</tt>.
	 * 
	 * The variant number has the following meaning:
	 * <p>
	 * <ul>
	 * <li>0 Reserved for NCS backward compatibility
	 * <li>2 The Leach-Salz variant (used by this class)
	 * <li>6 Reserved, Microsoft Corporation backward compatibility
	 * <li>7 Reserved for future definition
	 * </ul>
	 * 
	 * @return the variant number of this <tt>UUID</tt>.
	 */
	public int variant() {
		if (variant < 0) {
			// This field is composed of a varying number of bits
			if ((leastSigBits >>> 63) == 0) {
				variant = 0;
			} else if ((leastSigBits >>> 62) == 2) {
				variant = 2;
			} else {
				variant = (int) (leastSigBits >>> 61);
			}
		}
		return variant;
	}

	/**
	 * The timestamp value associated with this UUID.
	 * 
	 * <p>
	 * The 60 bit timestamp value is constructed from the time_low, time_mid, and time_hi fields of this <tt>UUID</tt>.
	 * The resulting timestamp is measured in 100-nanosecond units since midnight, October 15, 1582 UTC.
	 * <p>
	 * 
	 * The timestamp value is only meaningful in a time-based UUID, which has version type 1. If this <tt>UUID</tt> is
	 * not a time-based UUID then this method throws UnsupportedOperationException.
	 * 
	 * @throws UnsupportedOperationException
	 *             if this UUID is not a version 1 UUID.
	 */
	public long timestamp() {
		if (version() != 1) {
			throw new UnsupportedOperationException("Not a time-based UUID");
		}
		long result = timestamp;
		if (result < 0) {
			result = (mostSigBits & 0x0000000000000FFFL) << 48;
			result |= ((mostSigBits >> 16) & 0xFFFFL) << 32;
			result |= mostSigBits >>> 32;
			timestamp = result;
		}
		return result;
	}

	/**
	 * The clock sequence value associated with this UUID.
	 * 
	 * <p>
	 * The 14 bit clock sequence value is constructed from the clock sequence field of this UUID. The clock sequence
	 * field is used to guarantee temporal uniqueness in a time-based UUID.
	 * <p>
	 * 
	 * The clockSequence value is only meaningful in a time-based UUID, which has version type 1. If this UUID is not a
	 * time-based UUID then this method throws UnsupportedOperationException.
	 * 
	 * @return the clock sequence of this <tt>UUID</tt>.
	 * @throws UnsupportedOperationException
	 *             if this UUID is not a version 1 UUID.
	 */
	public int clockSequence() {
		if (version() != 1) {
			throw new UnsupportedOperationException("Not a time-based UUID");
		}
		if (sequence < 0) {
			sequence = (int) ((leastSigBits & 0x3FFF000000000000L) >>> 48);
		}
		return sequence;
	}

	/**
	 * The node value associated with this UUID.
	 * 
	 * <p>
	 * The 48 bit node value is constructed from the node field of this UUID. This field is intended to hold the IEEE
	 * 802 address of the machine that generated this UUID to guarantee spatial uniqueness.
	 * <p>
	 * 
	 * The node value is only meaningful in a time-based UUID, which has version type 1. If this UUID is not a
	 * time-based UUID then this method throws UnsupportedOperationException.
	 * 
	 * @return the node value of this <tt>UUID</tt>.
	 * @throws UnsupportedOperationException
	 *             if this UUID is not a version 1 UUID.
	 */
	public long node() {
		if (version() != 1) {
			throw new UnsupportedOperationException("Not a time-based UUID");
		}
		if (node < 0) {
			node = leastSigBits & 0x0000FFFFFFFFFFFFL;
		}
		return node;
	}

	public String toString() {
		return (digits(mostSigBits >> 32, 8) + digits(mostSigBits >> 16, 4) + digits(mostSigBits, 4) + digits(leastSigBits >> 48, 4) + digits(leastSigBits, 12));
	}

	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Long.toHexString(hi | (val & (hi - 1))).substring(1);
	}

	public int hashCode() {
		if (hashCode == -1) {
			hashCode = (int) ((mostSigBits >> 32) ^ mostSigBits ^ (leastSigBits >> 32) ^ leastSigBits);
		}
		return hashCode;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof UUID))
			return false;
		if (((UUID) obj).variant() != this.variant())
			return false;
		UUID id = (UUID) obj;
		return (mostSigBits == id.mostSigBits && leastSigBits == id.leastSigBits);
	}

	public int compareTo(UUID val) {
		return (this.mostSigBits < val.mostSigBits ? -1 : (this.mostSigBits > val.mostSigBits ? 1 : (this.leastSigBits < val.leastSigBits ? -1 : (this.leastSigBits > val.leastSigBits ? 1 : 0))));
	}

	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {

		in.defaultReadObject();

		version = -1;
		variant = -1;
		timestamp = -1;
		sequence = -1;
		node = -1;
		hashCode = -1;
	}

}
