package com.wl4g.component.rpc.codec.verify;

import com.wl4g.component.rpc.codec.ObjectCoder;
import com.wl4g.component.rpc.codec.CodecConfig.EndianType;
import com.wl4g.component.rpc.codec.CodecConfig.TotalLengthType;
import com.wl4g.component.rpc.codec.helper.ByteHelper;
import com.wl4g.component.rpc.codec.type.OCInt16;
import com.wl4g.component.rpc.codec.wrap.IdentifyWrapper;
import com.wl4g.component.rpc.codec.wrap.VerifyWrapper;

public class VerifyCoder {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ObjectCoder coder = new ObjectCoder();
		coder.getCodecConfig().setEndianType(EndianType.LITTLE);
		coder.getCodecConfig().setTotalLengthType(TotalLengthType.HEAD_BODY);
		coder.getCodecConfig().addWrap(new IdentifyWrapper(new OCInt16(0xfafb)));
		coder.getCodecConfig().addWrap(VerifyWrapper.CRC16());
		SimpleMsg msg = new SimpleMsg();
		msg.id = 32;
		msg.version = 1;
		msg.command = "running";
		byte[] bytes = coder.encode(msg);
		System.out.println(ByteHelper.toHexString(bytes));

		SimpleMsg result = new SimpleMsg();
		coder.decode(bytes, result);
		System.out.println("ID:" + result.id);
		System.out.println("VERSION:" + result.version);
		System.out.println("COMMAND:" + result.command);
	}

}
