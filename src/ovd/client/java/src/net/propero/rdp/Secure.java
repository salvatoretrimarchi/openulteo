/* Secure.java
 * Component: ProperJavaRDP
 * 
 * Revision: $Revision: 1.1.1.1 $
 * Author: $Author: suvarov $
 * Date: $Date: 2007/03/08 00:26:35 $
 *
 * Copyright (c) 2005 Propero Limited
 * Copyright (C) 2011-2012 Ulteo SAS
 * http://www.ulteo.com
 * Author David LECHEVALIER <david@ulteo.com> 2011, 2012
 * Author Thomas MOUTON <thomas@ulteo.com> 2012
 * Author Vincent ROULLIER <v.roullier@ulteo.com> 2012
 *
 * Purpose: Secure layer of communication
 */
package net.propero.rdp;
import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.*;
import java.math.*;
import org.apache.log4j.Logger;

import net.propero.rdp.crypto.*;
import net.propero.rdp.rdp5.SpooledPacket;
import net.propero.rdp.rdp5.VChannel;
import net.propero.rdp.rdp5.VChannels;
import net.propero.rdp.rdp5.rdpemt.EMTChannel;

public class Secure {
	boolean readCert = false;
	static Logger logger = Logger.getLogger(Secure.class);
	private Licence licence = null;
    /* constants for the secure layer */
    public static final int SEC_ENCRYPT = 0x0008;
    public static final int SEC_LOGON_INFO = 0x0040;

    static final int SEC_RANDOM_SIZE = 32;
    static final int SEC_MODULUS_SIZE = 64;
    static final int SEC_MAX_MODULUS_SIZE = 256;
    static final int SEC_PADDING_SIZE = 8;
    private static final int SEC_EXPONENT_SIZE = 4;

    private static final int SEC_CLIENT_RANDOM = 0x0001;
    static final int SEC_LICENCE_NEG = 0x0080;

    private static final int SEC_TAG_SRV_INFO = 0x0c01;
    private static final int SEC_TAG_SRV_CRYPT = 0x0c02;
    private static final int SEC_TAG_SRV_CHANNELS = 0x0c03;
    private static final int SC_MCS_MSGCHANNEL = 0x0c04;

    private static final int SEC_TAG_CLI_INFO = 0xc001;
    private static final int SEC_TAG_CLI_CRYPT = 0xc002;
    private static final int SEC_TAG_CLI_CHANNELS = 0xc003;
    private static final int SEC_TAG_CLI_4 = 0xc004;
    private static final int CS_MCS_MSGCHANNEL = 0xC006;

    private static final int SEC_TAG_PUBKEY = 0x0006;
    private static final int SEC_TAG_KEYSIG = 0x0008;

    private static final int SEC_RSA_MAGIC = 0x31415352; /* RSA1 */
	
	private static final int CONNECTION_TYPE_LAN              = 0x06;
	
	// earlyCapabilityFlags
	private static final int RNS_UD_CS_SUPPORT_ERRINFO_PDU         = 0x0001;
	private static final int RNS_UD_CS_WANT_32BPP_SESSION          = 0x0002;
	private static final int RNS_UD_CS_SUPPORT_STATUSINFO_PDU      = 0x0004;
	private static final int RNS_UD_CS_STRONG_ASYMMETRIC_KEYS      = 0x0008;
	private static final int RNS_UD_CS_VALID_CONNECTION_TYPE       = 0x0020;
	private static final int RNS_UD_CS_SUPPORT_MONITOR_LAYOUT_PDU  = 0x0040;
	private static final int RNS_UD_CS_SUPPORT_NETCHAR_AUTODETECT  = 0x0080;
	private static final int RNS_UD_CS_SUPPORT_DYNVC_GFX_PROTOCOL  = 0x0100;
	private static final int RNS_UD_CS_SUPPORT_DYNAMIC_TIME_ZONE   = 0x0200;
	
	private static final int ENCRYPTION_128BIT_FLAG           = 0x00000002;
	private static final int ENCRYPTION_40BIT_FLAG            = 0x00000001;
 
    private MCS McsLayer=null;
 //   private String hostname=null;
   // private String username=null;
    boolean licenceIssued = false;
    private RC4 rc4_enc = null;
    private RC4 rc4_dec = null;
    private RC4 rc4_update = null;
    private BlockMessageDigest sha1 = null;
    private BlockMessageDigest md5 = null;
    private int server_pubkey_length = SEC_MODULUS_SIZE;
    private int keylength = 0;
    private int enc_count = 0;
    private int dec_count = 0;
    
    private byte[] sec_sign_key = null;

    private byte[] sec_decrypt_key = null;

    private byte[] sec_encrypt_key = null;

    private byte[] sec_decrypt_update_key = null;

    private byte[] sec_encrypt_update_key = null;
    private byte[] sec_crypted_random=null;

    private byte[] exponent = null;
    private byte[] modulus = null;
    private byte[] server_random = null;
    private byte[] client_random = new byte[SEC_RANDOM_SIZE];
    
    private long lastNetworkActionTime;
    private BlockingQueue<SpooledPacket> spooledPacket;

    private static final byte[] pad_54 = {
	54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
	54, 54, 54,
	54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
	54, 54, 54
    };
    
    private static final byte[] pad_92 = {
	92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92,
	92, 92, 92, 92, 92, 92, 92,
	92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92,
	92, 92, 92, 92, 92, 92, 92
    };
    
    private VChannels channels;
    private Options opt = null;
    private Common common = null;
    public boolean ready = false;
    private EMTChannel emt = null;
    
    /**
     * Initialise Secure layer of communications
     * @param channels Virtual channels for this connection
     */
    public Secure(VChannels channels, Options opt_, Common common_) {
        this.channels = channels;
        this.common = common_;
        this.opt = opt_;
        this.licence = new Licence(this, this.opt, this.common);
	McsLayer = new MCS(channels, this.opt, this.common);
	this.common.mcs = McsLayer;
	rc4_dec = new RC4();
	rc4_enc = new RC4();
	rc4_update = new RC4();
	sha1 = new SHA1();
	md5 = new MD5();
	sec_sign_key = new byte[16]; // changed from 8 - rdesktop 1.2.0
	sec_decrypt_key = new byte[16];
	sec_encrypt_key = new byte[16];
	sec_decrypt_update_key = new byte[16]; // changed from 8 - rdesktop 1.2.0
	sec_encrypt_update_key = new byte[16]; // changed from 8 - rdesktop 1.2.0
	sec_crypted_random = new byte[64];
	this.ready = false;
	
	this.spooledPacket = new LinkedBlockingQueue<SpooledPacket>();
    }
    
    /**
     * Connect to server
     * @param host Address of server to connect to
     * @param port Port to connect to
     * @throws UnknownHostException
     * @throws IOException
     * @throws RdesktopException
     * @throws SocketException
     * @throws CryptoException
     * @throws OrderException
     */
    public void connect(String host, int port) throws UnknownHostException, IOException, RdesktopException, SocketException, CryptoException, OrderException {
	McsLayer.isoConnect(host, port);

	RdpPacket_Localised mcs_data = this.sendMcsData();
	McsLayer.connect(host, port, mcs_data);
	
	if (this.opt.encryption) {
	    this.establishKey();  
	}
    }
    
    /**
     * Connect to server on default port
     * @param host Server to connect to
     * @throws IOException
     * @throws RdesktopException
     * @throws OrderException
     * @throws CryptoException
     */
    public void connect(String host) throws IOException, RdesktopException, OrderException, CryptoException {
	this.connect(host, this.opt.port);
    }
    
    /**
     * Close connection
     */
    public void disconnect() {
	McsLayer.disconnect();
	}  

    /**
     * Construct MCS data, including channel, encryption and display options
     * @return Packet populated with MCS data
     */
    public RdpPacket_Localised sendMcsData() {
    	logger.debug("Secure.sendMcsData");
    
    RdpPacket_Localised buffer= new RdpPacket_Localised(512);
        
    int hostlen = 2 * (this.opt.clientName==null ? 0 : this.opt.clientName.length());
        
    if (hostlen > 30) { hostlen=30;}
            
	// We create the packet end in order to get it size
	buffer.incrementPosition(23);
    	
	// Client information
	buffer.setLittleEndian16(SEC_TAG_CLI_INFO);
	buffer.setLittleEndian16(this.opt.use_rdp5 ? 216 : 136);	/* total length */
	buffer.setLittleEndian16(this.opt.use_rdp5 ? 4 : 1);
	buffer.setLittleEndian16(8);
	buffer.setLittleEndian16(this.opt.width); 
	buffer.setLittleEndian16(this.opt.height); 
	buffer.setLittleEndian16(0xca01);
	buffer.setLittleEndian16(0xaa03);
	buffer.setLittleEndian32(this.opt.keylayout);
	buffer.setLittleEndian32(this.opt.use_rdp5 ? 2600 : 419); // or 0ece	// client build? we are 2600 compatible :-)

	/* Unicode name of client, padded to 32 bytes */
    buffer.outUnicodeString(this.opt.clientName.toUpperCase(), hostlen);
	buffer.incrementPosition(30 - hostlen);

	buffer.setLittleEndian32(4);
	buffer.setLittleEndian32(0);
	buffer.setLittleEndian32(12);
	buffer.incrementPosition(64);	/* reserved? 4 + 12 doublewords */

    buffer.setLittleEndian16(0xca01); // out_uint16_le(s, 0xca01);
	buffer.setLittleEndian16(this.opt.use_rdp5 ? 1 : 0);
    
	if(this.opt.use_rdp5){
	buffer.setLittleEndian32(0); // out_uint32(s, 0);
	buffer.set8(this.opt.server_bpp); // out_uint8(s, g_server_bpp);
	buffer.setLittleEndian16(0x0700); // out_uint16_le(s, 0x0700);
	buffer.set8(0); // out_uint8(s, 0);
	
	int earlyCapabilityFlags = RNS_UD_CS_SUPPORT_ERRINFO_PDU;
	if (this.opt.networkConnectionType != Rdp.CONNECTION_TYPE_UNKNOWN) {
		earlyCapabilityFlags |= RNS_UD_CS_VALID_CONNECTION_TYPE;
	}
	
	if (this.opt.networkConnectionType == Rdp.CONNECTION_TYPE_AUTODETECT && this.opt.extendedClientDataBlocksSupported) {
		earlyCapabilityFlags |= RNS_UD_CS_SUPPORT_NETCHAR_AUTODETECT;
	}
	
	buffer.setLittleEndian16(earlyCapabilityFlags); // out_uint32_le(s, 1);   /* earlyCapabilityFlags */    
	
	buffer.incrementPosition(64);
    	
	buffer.set8(this.opt.networkConnectionType);
	buffer.set8(0);    /* pad1octet */
	int selectecProto = ISO.PROTOCOL_RDP;
	if (this.opt.useTLS)
		selectecProto |= ISO.PROTOCOL_SSL;
	buffer.setLittleEndian32(selectecProto); /* serverSelectedProtocol */

	buffer.setLittleEndian16(SEC_TAG_CLI_4); // out_uint16_le(s, SEC_TAG_CLI_4);
	buffer.setLittleEndian16(12); // out_uint16_le(s, 12);
	buffer.setLittleEndian32(this.opt.console_session ? 0xb : 0xd); // out_uint32_le(s, g_console_session ? 0xb : 9);
	buffer.setLittleEndian32(0); // out_uint32(s, 0);
    }
    
	// Client encryption settings //
	buffer.setLittleEndian16(SEC_TAG_CLI_CRYPT);
	buffer.setLittleEndian16(this.opt.use_rdp5 ? 12 : 8);	// length

	if (this.opt.useTLS) {
		buffer.setLittleEndian32(0);
	} else {
		buffer.setLittleEndian32(ENCRYPTION_40BIT_FLAG | ENCRYPTION_128BIT_FLAG);
	}
	
	if(this.opt.use_rdp5) buffer.setLittleEndian32(0); // unknown
	
	if (this.opt.use_rdp5 && (channels.num_channels() > 0))
	{
	    logger.debug(("num_channels is " + channels.num_channels()));
		buffer.setLittleEndian16(SEC_TAG_CLI_CHANNELS); // out_uint16_le(s, SEC_TAG_CLI_CHANNELS);
		buffer.setLittleEndian16(channels.num_channels() * 12 + 8); // out_uint16_le(s, g_num_channels * 12 + 8);	// length 
		buffer.setLittleEndian32(channels.num_channels()); // out_uint32_le(s, g_num_channels);	// number of virtual channels 
		for (int i = 0; i < channels.num_channels(); i++)
		{
			logger.debug(("Requesting channel " + channels.channel(i).name()));
			buffer.out_uint8p(channels.channel(i).name(), 8); // out_uint8a(s, g_channels[i].name, 8);
			buffer.setBigEndian32(channels.channel(i).flags()); // out_uint32_be(s, g_channels[i].flags);
		}
	}
	
	if (this.opt.extendedClientDataBlocksSupported) {
		buffer.setLittleEndian16(CS_MCS_MSGCHANNEL);  // type
		buffer.setLittleEndian16(8);                  // length
		buffer.setLittleEndian32(0);                  // flags (must be set to 0)
	}
	
	buffer.markEnd();
	
	// We create the packet begin using the length computed by the previous part 
	int length = buffer.getPosition() - 23;
	buffer.setPosition(0);
	buffer.setBigEndian16(5);	/* unknown */
	buffer.setBigEndian16(0x14);
	buffer.set8(0x7c);
	buffer.setBigEndian16(1);
	
	buffer.setBigEndian16((length + 14) | 0x8000);	// remaining length
	
	buffer.setBigEndian16(8);	// length?
	buffer.setBigEndian16(16);
	buffer.set8(0);
	buffer.setLittleEndian16(0xc001);
	buffer.set8(0);
	
	buffer.setLittleEndian32(0x61637544);	// "Duca" ?!
	buffer.setBigEndian16(length | 0x8000);	// remaining length	
    	
	return buffer;
    }

    /**
     * Handle MCS info from server (server info, encryption info and channel information)
     * @param mcs_data Data received from server
     */    
    public void processMcsData(RdpPacket_Localised mcs_data) throws RdesktopException, CryptoException {
	logger.debug("Secure.processMcsData");
	int tag=0, len=0, length=0, nexttag=0;
	
	mcs_data.incrementPosition(21); // header (T.124 stuff, probably)
	len=mcs_data.get8();

	if ((len&0x00000080)!=0) {
	    len=mcs_data.get8();
	}

	while (mcs_data.getPosition() < mcs_data.getEnd()) {
	    tag=mcs_data.getLittleEndian16();
	    length=mcs_data.getLittleEndian16();

	    if (length <=4) return;
	    
	    nexttag=mcs_data.getPosition() + length -4;
	    
	    switch(tag) {
	    case (Secure.SEC_TAG_SRV_INFO): 
	    processSrvInfo(mcs_data);
	    break;
	    case (Secure.SEC_TAG_SRV_CRYPT):
		this.processCryptInfo(mcs_data, length - 4);
		break;
		case (Secure.SEC_TAG_SRV_CHANNELS):
			/* FIXME: We should parse this information and
			   use it to map RDP5 channels to MCS 
			   channels */
			break;
		
		case Secure.SC_MCS_MSGCHANNEL:
			int transportID = mcs_data.getLittleEndian16();
			this.emt = new EMTChannel(this.opt, this.common);
			emt.set_mcs_id(transportID);
			this.channels.register(emt);
			break;

	    default: throw new RdesktopException("Not implemented! Tag:"+tag+"not recognized!");
	    }

	    mcs_data.setPosition(nexttag);
	}
    }

    /**
     * Read server info from packet, specifically the RDP version of the server
     * @param mcs_data Packet to read
     */
	private void processSrvInfo(RdpPacket_Localised mcs_data) {
		this.opt.server_rdp_version = mcs_data.getLittleEndian16(); // in_uint16_le(s, g_server_rdp_version);
		logger.debug(("Server RDP version is " +  this.opt.server_rdp_version));
		if (1 == this.opt.server_rdp_version)
			this.opt.use_rdp5 = false;
	}

    public void establishKey()throws RdesktopException, IOException, CryptoException {
	int length = this.server_pubkey_length + SEC_PADDING_SIZE;
	int flags = SEC_CLIENT_RANDOM;
	RdpPacket_Localised buffer = this.init(flags, length + 4);

	buffer.setLittleEndian32(length);

	buffer.copyFromByteArray(this.sec_crypted_random, 0, buffer.getPosition(), this.server_pubkey_length);
	buffer.incrementPosition(this.server_pubkey_length);
	buffer.incrementPosition(SEC_PADDING_SIZE);
	buffer.markEnd();
	this.send(buffer, flags);
	
    }

    public void processCryptInfo(RdpPacket_Localised data, int length) throws RdesktopException, CryptoException {
	int rc4_key_size=0;
	    
	rc4_key_size = this.parseCryptInfo(data, length);
	if (rc4_key_size == 0) {
	    return;
	}
	
	//this.client_random = this.generateRandom(SEC_RANDOM_SIZE);
	logger.debug("readCert = " + readCert);
	if (readCert)
	{			/* Which means we should use 
				   RDP5-style encryption */
		
		
		// *** reverse the client random
		//this.reverse(this.client_random);
		
		// *** load the server public key into the stored data for encryption
		/* this.exponent = this.server_public_key.getPublicExponent().toByteArray();
		this.modulus = this.server_public_key.getModulus().toByteArray();
		
		System.out.println("Exponent: " + server_public_key.getPublicExponent());
		System.out.println("Modulus: " + server_public_key.getModulus());
		*/
		
		// *** perform encryption
		//this.sec_crypted_random = RSA_public_encrypt(this.client_random, this.server_public_key);
		//this.RSAEncrypt(SEC_RANDOM_SIZE);
		
		//this.RSAEncrypt(SEC_RANDOM_SIZE);
		
		// *** reverse the random data back
		//this.reverse(this.sec_crypted_random);

	}
	else{
		this.generateRandom();
		this.RSAEncrypt(SEC_RANDOM_SIZE);
	}
	this.generate_keys(rc4_key_size);
    }

    /**
     * Intialise a packet at the Secure layer
     * @param flags Encryption flags
     * @param length Length of packet
     * @return Intialised packet
     * @throws RdesktopException
     */
    public RdpPacket_Localised init(int flags, int length) throws RdesktopException {
	int headerlength=0;
	RdpPacket_Localised buffer;
 
	if (!this.licenceIssued) 
	    headerlength = ((flags & SEC_ENCRYPT)!=0) ? 12 : 4;
	else 
	     headerlength = ((flags & SEC_ENCRYPT)!=0) ? 12 : 0;
	
	buffer=McsLayer.init(length+headerlength);
	buffer.pushLayer(RdpPacket_Localised.SECURE_HEADER,headerlength);
	//buffer.setHeader(RdpPacket_Localised.SECURE_HEADER);
	//buffer.incrementPosition(headerlength);
	//buffer.setStart(buffer.getPosition());
	return buffer;
    }

    /**
     * Send secure data on the global channel
     * @param sec_data Data to send
     * @param flags Encryption flags
     * @throws RdesktopException
     * @throws IOException
     * @throws CryptoException
     */
	public void send(RdpPacket_Localised sec_data, int flags) throws RdesktopException, IOException, CryptoException {
		this.lastNetworkActionTime = new GregorianCalendar().getTimeInMillis();
		send_to_channel(sec_data,flags,MCS.MCS_GLOBAL_CHANNEL);
	}
    
    /**
     * Prepare data as a Secure PDU and pass down to the MCS layer
     * @param sec_data Data to send
     * @param flags Encryption flags
     * @param channel Channel over which to send data
     * @throws RdesktopException
     * @throws IOException
     * @throws CryptoException
     */
    public synchronized void send_to_channel(RdpPacket_Localised sec_data, int flags, int channel) throws RdesktopException, IOException, CryptoException {
	int datalength=0;
	byte[] signature = null;
	byte[] data;
	byte[] buffer;

	sec_data.setPosition(sec_data.getHeader(RdpPacket_Localised.SECURE_HEADER));
	
	if (this.licenceIssued == false || (flags & SEC_ENCRYPT) !=0) {
	    sec_data.setLittleEndian32(flags);
	}
	if ((flags & SEC_ENCRYPT) !=0) {
	    flags &= ~SEC_ENCRYPT;
	    datalength = sec_data.getEnd() - sec_data.getPosition() - 8;
	    data = new byte[datalength];
	    buffer = null;
	    sec_data.copyToByteArray(data, 0, sec_data.getPosition()+8, datalength); 
	    signature = this.sign(this.sec_sign_key, 8, this.keylength, data, datalength);
	   
	    buffer = this.encrypt(data, datalength);
	    
	    sec_data.copyFromByteArray(signature, 0, sec_data.getPosition(), 8);
	    sec_data.copyFromByteArray(buffer, 0, sec_data.getPosition()+8, datalength);
	   
	}
	//McsLayer.send(sec_data);
    McsLayer.send_to_channel(sec_data,channel);
    }
    
    public void spool_packet(RdpPacket_Localised sec_data, int flags, int channel) throws RdesktopException, IOException, CryptoException {
    	VChannel v = channels.find_channel_by_channelno(channel);

    	if (v.packetStat < v.packetLimit) {
    		v.send_packet(sec_data, false);
    		v.packetStat += sec_data.size();
    		return;
    	}
		
    	SpooledPacket p = new SpooledPacket(sec_data, channel);

    	try {
    		this.spooledPacket.add(p);
    		return;
    	}
    	catch (IllegalStateException e) {
    		logger.debug("Packet spool is full, send it normally");
    	}
    	catch (ClassCastException e) {
    		logger.error("Unable to spool packet, invalid packet");
    	}
    	catch (NullPointerException e) {
    		logger.error("Unable to spool packet, packet is null");
    	}
    	catch (IllegalArgumentException e) {
    		logger.error("Unable to spool packet, packet is not valid for the spool");
    	}
    	
    	this.send_to_channel(sec_data, flags, channel);
    }
    
    public void processSpooledPacket() throws RdesktopException, IOException, CryptoException {
    	if (! this.opt.useBandwithLimitation)
    		return;
    	
    	long currentTime = new GregorianCalendar().getTimeInMillis();

    	if (! this.spooledPacket.isEmpty()) {
    		SpooledPacket p = this.spooledPacket.peek();
    		if (p == null)
    			return;
    		
    		VChannel v = channels.find_channel_by_channelno(p.getChannel());
			
    		if (currentTime - v.lastTime >= 1000) {
    			v.lastTime = currentTime;
    			v.packetStat = 0;
    		}

    		if (v.packetStat > v.packetLimit)
    			return;

    		v.send_packet(p.getPacket(), false);
    		v.packetStat += p.getPacket().size();
    		spooledPacket.poll();
    	}
    }
    

    /**
     * Generate MD5 signature
     * @param session_key Key with which to sign data
     * @param length Length of signature
     * @param keylen Length of key
     * @param data Data to sign
     * @param datalength Length of data to sign
     * @return Signature for data
     * @throws CryptoException
     */
    public byte[] sign(byte[] session_key, int length, int keylen, byte[] data, int datalength) throws CryptoException {
	byte[] shasig = new byte[20];
	byte[] md5sig = new byte[16];
	byte[] lenhdr = new byte[4];
	byte[] signature = new byte[length];

	this.setLittleEndian32(lenhdr, datalength);
	
	sha1.engineReset();
	sha1.engineUpdate(session_key, 0, keylen/*length*/);
	sha1.engineUpdate(pad_54, 0, 40);
	sha1.engineUpdate(lenhdr, 0, 4);
	sha1.engineUpdate(data, 0, datalength);
	shasig = sha1.engineDigest();
	sha1.engineReset();

	md5.engineReset();
	md5.engineUpdate(session_key, 0, keylen/*length*/);
	md5.engineUpdate(pad_92, 0, 48);
	md5.engineUpdate(shasig, 0, 20);
	md5sig = md5.engineDigest();
	md5.engineReset();

	System.arraycopy(md5sig, 0, signature, 0, length);
	return signature;
    }

    /**
     * Encrypt specified number of bytes from provided data using RC4 algorithm
     * @param data Data to encrypt
     * @param length Number of bytes to encrypt (from start of array)
     * @return Encrypted data
     * @throws CryptoException
     */
    public byte[] encrypt(byte[] data, int length) throws CryptoException {
	byte[] buffer = null;
	if (this.enc_count==4096) {
	    sec_encrypt_key = this.update(this.sec_encrypt_key, this.sec_encrypt_update_key);
	    byte[] key = new byte[this.keylength];
	    System.arraycopy(this.sec_encrypt_key, 0, key, 0, this.keylength);
	    this.rc4_enc.engineInitEncrypt(key);
//		logger.debug("Packet enc_count="+enc_count);
		 this.enc_count=0;
	}
	//this.rc4.engineInitEncrypt(this.rc4_encrypt_key);
	buffer = this.rc4_enc.crypt(data, 0, length);
	this.enc_count++;
	return buffer;
    }
    
    /**
     * Encrypt provided data using the RC4 algorithm
     * @param data Data to encrypt
     * @return Encrypted data
     * @throws CryptoException
     */
    public byte[] encrypt(byte[] data) throws CryptoException {
	byte[] buffer = null;
	if (this.enc_count==4096) {
	    sec_encrypt_key = this.update(this.sec_encrypt_key, this.sec_encrypt_update_key);
	    byte[] key = new byte[this.keylength];
	    System.arraycopy(this.sec_encrypt_key, 0, key, 0, this.keylength);
	    this.rc4_enc.engineInitEncrypt(key);
	//	logger.debug("Packet enc_count="+enc_count);
		this.enc_count=0;
	}
//	this.rc4.engineInitEncrypt(this.rc4_encrypt_key);
	
	buffer = this.rc4_enc.crypt(data);
	this.enc_count++;
	return buffer;
    }

    /**
     * Decrypt specified number of bytes from provided data using RC4 algorithm
     * @param data Data to decrypt
     * @param length Number of bytes to decrypt (from start of array)
     * @return Decrypted data
     * @throws CryptoException
     */
    public byte[] decrypt(byte[] data, int length) throws CryptoException {
	byte[] buffer = null;
	if (this.dec_count==4096) {
	    sec_decrypt_key = this.update(this.sec_decrypt_key, this.sec_decrypt_update_key);
	    byte[] key = new byte[this.keylength];
	    System.arraycopy(this.sec_decrypt_key, 0, key, 0, this.keylength);
	    this.rc4_dec.engineInitDecrypt(key);
//		logger.debug("Packet dec_count="+dec_count);
		this.dec_count=0;
	}
	//this.rc4.engineInitDecrypt(this.rc4_decrypt_key);
	buffer = this.rc4_dec.crypt(data, 0, length);
	this.dec_count++;
	return buffer;
    }
    
    /**
     * Decrypt provided data using RC4 algorithm
     * @param data Data to decrypt
     * @return Decrypted data
     * @throws CryptoException
     */
    public byte[] decrypt(byte[] data) throws CryptoException {
	byte[] buffer = null;
	if (this.dec_count==4096) {
	    sec_decrypt_key = this.update(this.sec_decrypt_key, this.sec_decrypt_update_key);
	    byte[] key = new byte[this.keylength];
	    System.arraycopy(this.sec_decrypt_key, 0, key, 0, this.keylength);
	    this.rc4_dec.engineInitDecrypt(key);
//		logger.debug("Packet dec_count="+dec_count);
		this.dec_count=0;
	}
	//this.rc4.engineInitDecrypt(this.rc4_decrypt_key);
	
	buffer = this.rc4_dec.crypt(data);
	this.dec_count++;
	return buffer;
    }
    
    /**
     * Read encryption information from a Secure layer PDU, obtaining and storing
     * level of encryption and any keys received
     * @param data Packet to read encryption information from
     * @return Size of RC4 key
     * @throws RdesktopException
     */
    public int parseCryptInfo(RdpPacket_Localised data, int dataLen) throws RdesktopException {
	logger.debug("Secure.parseCryptInfo");
	int endPacket = data.getPosition() + dataLen; 
	
	int encryption_level=0, random_length=0, RSA_info_length=0;
	int tag=0, length=0;
	int next_tag=0, end=0; 
	int rc4_key_size=0;

	rc4_key_size = data.getLittleEndian32(); // 1 = 40-Bit 2 = 128 Bit
	encryption_level = data.getLittleEndian32(); // 1 = low, 2 = medium, 3 = high
	if (encryption_level==0) { // no encryption
	    return 0;
	}
	random_length = data.getLittleEndian32();
	RSA_info_length = data.getLittleEndian32();

	if ( random_length != SEC_RANDOM_SIZE) {
	    throw new RdesktopException("Wrong Size of Random! Got" + random_length + "expected" + SEC_RANDOM_SIZE);
	}
	this.server_random = new byte[random_length];
	data.copyToByteArray(this.server_random, 0, data.getPosition(), random_length);
	data.incrementPosition(random_length);

	end = data.getPosition() + RSA_info_length;

	if (end > endPacket) {
	   logger.debug("Reached end of crypt info prematurely ");
	    return 0;
	}

	//data.incrementPosition(12); // unknown bytes
	int flags = data.getLittleEndian32(); // in_uint32_le(s, flags);	// 1 = RDP4-style, 0x80000002 = X.509
	logger.debug("Flags = 0x" + Integer.toHexString(flags));
	if ((flags & 1) != 0)
	{
		logger.debug(("We're going for the RDP4-style encryption"));
		data.incrementPosition(8); //in_uint8s(s, 8);	// unknown

	while (data.getPosition() < endPacket) {
	    tag=data.getLittleEndian16();
	    length=data.getLittleEndian16();

	    next_tag = data.getPosition() + length;

	    switch (tag) {

	    case (Secure.SEC_TAG_PUBKEY):
		
		if (!parsePublicKey(data)) {
		    return 0;
		}
		
		break;
	    case (Secure.SEC_TAG_KEYSIG):
		// Microsoft issued a key but we don't care
		break;

	    default:
		throw new RdesktopException("Unimplemented decrypt tag "+tag);
	    }
	    data.setPosition(next_tag);
	}

	if (data.getPosition() == endPacket) {
	    return rc4_key_size;
	} else {
	    logger.warn("End not reached!");
	    return 0;
	}
	
	}else{
		//data.incrementPosition(4); // number of certificates
		@SuppressWarnings("unused")
		int num_certs = data.getLittleEndian32();
		
		int cacert_len = data.getLittleEndian32();
		data.incrementPosition(cacert_len);
		int cert_len = data.getLittleEndian32();
		data.incrementPosition(cert_len);
		
		readCert = true;
		
		return rc4_key_size;
	}
	
	
    }
    /*
    public X509Certificate readCert(int length, RdpPacket_Localised data){  	
    	byte[] buf = new byte[length];
    	
    	data.copyToByteArray(buf,0,data.getPosition(),buf.length);
    	data.incrementPosition(length);	
    	
    	for(int i = 0; i < buf.length; i++){
    		buf[i] = (byte) (buf[i] & 0xFF);
    	}
    	
    	ByteArrayInputStream bIn = new ByteArrayInputStream(buf);
    	X509Certificate cert = null;
    	CertificateFactory cf = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
			cert = (X509Certificate)cf.generateCertificate(bIn);
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	bIn.reset();
    	return cert;
    }
*/
    public void generateRandom() {
	/*try{
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.nextBytes(this.client_random);
    } catch(NoSuchAlgorithmException e){logger.warn("No Such Random Algorithm");}*/
    }

    public void RSAEncrypt(int length) throws RdesktopException {
	byte[] inr = new byte[SEC_MAX_MODULUS_SIZE];
	//int outlength = 0;
	BigInteger mod = null;
	BigInteger exp = null;
	BigInteger x   = null;

	this.reverse(this.exponent);
	this.reverse(this.modulus);
	System.arraycopy(this.client_random, 0, inr, 0, length);
	this.reverse(inr);

	if ((this.modulus[0] & 0x80) != 0) {
	    byte[] temp = new byte [this.modulus.length+1];
	    System.arraycopy(this.modulus, 0, temp, 1, this.modulus.length);
	    temp[0]=0;
	    mod = new BigInteger(temp);
	} else {
	    mod = new BigInteger(this.modulus);
	}
	if ((this.exponent[0] & 0x80) != 0) {
	    byte[] temp = new byte [this.exponent.length+1];
	    System.arraycopy(this.exponent, 0, temp, 1, this.exponent.length);
	    temp[0]=0;
	    exp = new BigInteger(temp);
	} else {
	    exp = new BigInteger(this.exponent);
	}
	if ((inr[0] & 0x80) != 0) {
	    byte[] temp = new byte [inr.length+1];
	    System.arraycopy(inr, 0, temp, 1, inr.length);
	    temp[0]=0;
	    x = new BigInteger(temp);
	} else {
	    x   = new BigInteger(inr);
	}
	
	BigInteger y = x.modPow(exp, mod);
	this.sec_crypted_random = y.toByteArray();

	if ((this.sec_crypted_random[0] & 0x80) != 0) {
	    throw new RdesktopException("Wrong Sign! Expected positive Integer!");
	}
	
	if (this.sec_crypted_random.length > SEC_MODULUS_SIZE) {
	    logger.warn("sec_crypted_random too big!"); /* FIXME */
	}
	this.reverse(this.sec_crypted_random);

	byte[] temp = new byte[this.server_pubkey_length];

	if (this.sec_crypted_random.length < SEC_MODULUS_SIZE) {
	    System.arraycopy(this.sec_crypted_random, 0, temp, 0, this.sec_crypted_random.length);
	    for (int i = this.sec_crypted_random.length; i < temp.length; i++) {
		temp[i] = 0;
	    }
	    this.sec_crypted_random = temp;
	    
	}

    }

    

    /**
     * Read in a public key from a provided Secure layer PDU, and store
     * in this.exponent and this.modulus
     * @param data Secure layer PDU containing key data
     * @return True if key successfully read
     * @throws RdesktopException
     */
    public boolean parsePublicKey(RdpPacket_Localised data) throws RdesktopException {
	int magic=0, modulus_length=0;

	magic = data.getLittleEndian32();

	if (magic != SEC_RSA_MAGIC) {
	    throw new RdesktopException("Wrong magic! Expected" + SEC_RSA_MAGIC + "got:" + magic);
	}

	modulus_length = data.getLittleEndian32();
	
	modulus_length -= SEC_PADDING_SIZE;
	if (modulus_length < SEC_MODULUS_SIZE || modulus_length > SEC_MAX_MODULUS_SIZE) {
	    throw new RdesktopException("Wrong modulus size! Expected" + SEC_MODULUS_SIZE + "+" + SEC_PADDING_SIZE + "got:" + modulus_length);
	}
	
	data.incrementPosition(8); //unknown modulus bits
	this.exponent = new byte[SEC_EXPONENT_SIZE];
	data.copyToByteArray(this.exponent, 0, data.getPosition(), SEC_EXPONENT_SIZE);	
	data.incrementPosition(SEC_EXPONENT_SIZE);
	this.modulus = new byte[modulus_length];
	data.copyToByteArray(this.modulus, 0, data.getPosition(), modulus_length);
	data.incrementPosition(modulus_length);
	data.incrementPosition(SEC_PADDING_SIZE);

	this.server_pubkey_length = modulus_length;
	if (data.getPosition() <= data.getEnd()) {
	    return true;
	} else {
	    return false;
	}
    }
    
    /**
     * Reverse the values in the provided array
     * @param data Array as passed reversed on return
     */
    public void reverse(byte[] data) {
	int i=0, j=0;
	byte temp = 0;

	for(i=0, j=data.length-1; i < j; i++, j--) {
	    temp = data[i];
	    data[i] = data[j];
	    data[j] = temp;
	}
    }
       
     public void reverse(byte[] data, int length) {
	 int i=0, j=0;
	byte temp = 0;
	
	for(i=0, j=length-1; i < j; i++, j--) {
	    temp = data[i];
	    data[i] = data[j];
	    data[j] = temp;
	}
     }

    public byte[] hash48(byte[] in, byte[] salt1, byte[] salt2, int salt)throws CryptoException {
	byte[] shasig = new byte[20];
	byte[] pad = new byte[4];
	byte[] out = new byte[48];
	int i = 0;
	
	for(i=0; i< 3; i++) {
	    for(int j=0; j <=i; j++) {
		pad[j]=(byte)(salt+i);
	    }
	    sha1.engineUpdate(pad, 0, i + 1);
	    sha1.engineUpdate(in, 0, 48);
	    sha1.engineUpdate(salt1, 0, 32);
	    sha1.engineUpdate(salt2, 0, 32);
	    shasig = sha1.engineDigest();
	    sha1.engineReset();

	    md5.engineUpdate(in, 0, 48);
	    md5.engineUpdate(shasig, 0, 20);
	    System.arraycopy(md5.engineDigest(), 0, out, i*16, 16);
	}

	return out;
    }
	
    public byte[] hash16(byte[] in, byte[] salt1, byte[] salt2, int in_position) throws CryptoException {
	
	md5.engineUpdate(in, in_position, 16);
	md5.engineUpdate(salt1, 0, 32);
	md5.engineUpdate(salt2, 0, 32);
	return md5.engineDigest();
    }

    /**
     * Generate a 40-bit key and store in the parameter key.
     * @param key
     */
    public void make40bit(byte[] key) {
	key[0] = (byte)0xd1;
	key[1] = (byte)0x26;
	key[2] = (byte)0x9e;
    }

    /**
     * 
     * @param key
     * @param update_key
     * @return
     * @throws CryptoException
     */
    public byte[] update(byte[] key, byte[] update_key) throws CryptoException {
	byte[] shasig = new byte[20];
	byte[] update = new byte[this.keylength]; // changed from 8 - rdesktop 1.2.0
	byte[] thekey = new byte[key.length];

	sha1.engineReset();
    sha1.engineUpdate(update_key, 0, keylength); 
	sha1.engineUpdate(pad_54, 0, 40);
	sha1.engineUpdate(key,0 , keylength); // changed from 8 - rdesktop 1.2.0
	shasig = sha1.engineDigest();
	sha1.engineReset();

	md5.engineReset();
	md5.engineUpdate(update_key,0 ,keylength); // changed from 8 - rdesktop 1.2.0
	md5.engineUpdate(pad_92, 0, 48);
	md5.engineUpdate(shasig, 0, 20);
	thekey = md5.engineDigest();
	md5.engineReset();

	System.arraycopy(thekey, 0, update, 0, this.keylength);
	rc4_update.engineInitDecrypt(update);
	// added
	thekey = rc4_update.crypt(thekey, 0, this.keylength);

	if( this.keylength == 8) {
	    this.make40bit(thekey);
	}
	
	return thekey;
    }

    /**
     * Write a 32-bit integer value to an array of bytes, length 4
     * @param data Modified by method to be a 4-byte array representing the parameter value
     * @param value Integer value to return as a little-endian 32-bit value
     */
    public void setLittleEndian32(byte[] data, int value) {

	data[3] = (byte)((value >>> 24) & 0xff); 
	data[2] = (byte)((value >>> 16) & 0xff); 
	data[1] = (byte)((value >>> 8) & 0xff); 
	data[0] = (byte)(value & 0xff); 
    }

    /**
     * Receive a Secure layer PDU from the MCS layer
     * 
     * @return Packet representing received Secure PDU
     * @throws RdesktopException
     * @throws IOException
     * @throws CryptoException
     * @throws OrderException
     */
    public RdpPacket_Localised receive() throws RdesktopException, IOException, CryptoException, OrderException {
	int sec_flags=0;
	RdpPacket_Localised buffer=null;
	
	this.processSpooledPacket();
	
	while(true) {
		int[] channel = new int[1];
		try {
			buffer=McsLayer.receive(channel);
		}catch (SocketTimeoutException e){
			this.processSpooledPacket();
			long time = new GregorianCalendar().getTimeInMillis();
			
			if (this.opt.useKeepAlive && ((time - this.lastNetworkActionTime) > this.opt.keepAliveInterval * 1000))
				this.sendKeepAlive();
			
			continue;
		}
		if(buffer==null) return null;
		
		this.lastNetworkActionTime = new GregorianCalendar().getTimeInMillis();

		if (this.opt.server_rdp_version != 3) {
			if ((this.opt.server_rdp_version & 0x80) != 0) {
				buffer.incrementPosition(8);
				byte[] data = new byte[buffer.size() - buffer.getPosition()];
				buffer.copyToByteArray(data, 0, buffer.getPosition(), data.length);
				byte[] packet = this.decrypt(data);

				buffer.copyFromByteArray(packet, 0, buffer.getPosition(), packet.length);
			}
			return buffer;
    		}

		buffer.setHeader(RdpPacket_Localised.SECURE_HEADER);
		if (this.opt.encryption || (! this.licenceIssued)) {

			sec_flags=buffer.getLittleEndian32();

			if ((sec_flags & SEC_LICENCE_NEG) != 0) {
				licence.process(buffer);
				this.ready = true;
				continue;
			}
			if ((sec_flags & SEC_ENCRYPT) != 0) {
				buffer.incrementPosition(8); //signature
				byte[] data = new byte[buffer.size() - buffer.getPosition()];
				buffer.copyToByteArray(data, 0, buffer.getPosition(), data.length);
				byte[] packet = this.decrypt(data);
				
				buffer.copyFromByteArray(packet, 0, buffer.getPosition(), packet.length);
			}
		}
		
		if ((this.emt != null) && (this.emt.mcs_id() == channel[0])) {
			this.emt.process(buffer);
			this.opt.server_rdp_version = 0xff;
			return buffer;
		}
	    
    		if (channel[0] != MCS.MCS_GLOBAL_CHANNEL)
    		{
    			channels.channel_process(buffer, channel[0]);
			this.opt.server_rdp_version = 0xff;
    			return buffer;
    		}
    		
    		buffer.setStart(buffer.getPosition());
    		return buffer;
    	}
    }

	/**
	 * Send a neutral packet in order to check the connectivity
	 * @throws RdesktopException
	 * @throws IOException
	 * @throws CryptoException
	 */
	private void sendKeepAlive() throws RdesktopException, IOException, CryptoException {
		RdpPacket_Localised buffer = null;
		System.out.println("send keep alive");
		buffer = this.init(this.opt.encryption ? Secure.SEC_ENCRYPT : 0, 22);
		buffer.pushLayer(RdpPacket_Localised.RDP_HEADER, 18);
		
		buffer.setLittleEndian16(1); // type
		buffer.setLittleEndian16(1002);
		
		buffer.markEnd();
		CommunicationMonitor.lock(this);
		
		int length;
		
		buffer.setPosition(buffer.getHeader(RdpPacket_Localised.RDP_HEADER));
		length = buffer.getEnd() - buffer.getPosition();
		
		buffer.setLittleEndian16(length);
		buffer.setLittleEndian16(0x7 | 0x10);
		buffer.setLittleEndian16(this.getUserID() + 1001);
		
		buffer.setLittleEndian32(0);
		buffer.set8(0); // pad
		buffer.set8(1); // stream id
		buffer.setLittleEndian16(length - 14);
		buffer.set8(0x1f);
		buffer.set8(0); // compression type
		buffer.setLittleEndian16(0); // compression length
		
		this.send(buffer, this.opt.encryption ? Secure.SEC_ENCRYPT : 0);
		
		CommunicationMonitor.unlock(this);
	}
	
    /**
     * Generate encryption keys of applicable size for connection
     * @param rc4_key_size Size of keys to generate (1 if 40-bit encryption, otherwise 128-bit)
     * @throws CryptoException
     */
	public void generate_keys(int rc4_key_size) throws CryptoException {
			byte[] session_key = new byte[48];
			byte[] temp_hash = new byte[48];
			byte[] input = new byte[48];
	
			System.arraycopy(this.client_random, 0, input, 0, 24);
			System.arraycopy(this.server_random, 0, input, 24, 24);

			temp_hash = this.hash48(input, this.client_random, this.server_random, 65);
			session_key = this.hash48(temp_hash, this.client_random, this.server_random, 88);

			System.arraycopy(session_key, 0, this.sec_sign_key, 0, 16); // changed from 8 - rdesktop 1.2.0
	
			this.sec_decrypt_key = this.hash16(session_key, this.client_random, this.server_random, 16);
			this.sec_encrypt_key = this.hash16(session_key, this.client_random, this.server_random, 32);
	
			if (rc4_key_size == 1) {
				logger.info("40 Bit Encryption enabled");
				this.make40bit(this.sec_sign_key);
				this.make40bit(this.sec_decrypt_key);
				this.make40bit(this.sec_encrypt_key);
				this.keylength = 8;
			} else {
				logger.info("128 Bit Encryption enabled");
				this.keylength = 16;
			}
	
			System.arraycopy(this.sec_decrypt_key, 0, this.sec_decrypt_update_key, 0, 16); // changed from 8 - rdesktop 1.2.0
			System.arraycopy(this.sec_encrypt_key, 0, this.sec_encrypt_update_key, 0, 16); // changed from 8 - rdesktop 1.2.0

	
			byte[] key = new byte[this.keylength];
			System.arraycopy(this.sec_encrypt_key, 0, key, 0, this.keylength);
			rc4_enc.engineInitEncrypt(key);
			System.arraycopy(this.sec_decrypt_key, 0, key, 0, this.keylength);
			rc4_dec.engineInitDecrypt(key);
			}

    /**
     * @return MCS user ID
     */
    public int getUserID() {
	return McsLayer.getUserID();
    }
}
