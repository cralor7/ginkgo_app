package com.qk.nfc;

import java.io.ByteArrayOutputStream;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 * ToStringHex
 */
public class ToStringHex {

	/**
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	private static String hexString = "0123456789ABCDEF";
	/**
	* 将字符串编码成16进制数字,适用于所有字符（包括中文）
	*/
	public static String encode(String str)
	{
		//根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length*2);
		//将字节数组中每个字节拆解成2位16进制整数
		for(int i = 0; i<bytes.length; i++)
		{
			sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
			sb.append(hexString.charAt((bytes[i]&0x0f)>>0));
		}
		return sb.toString();
	}

	//字符串转16进制
	public static String str2HexStr(String str)
	{

		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;

		for (int i = 0; i < bs.length; i++)
		{
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
			sb.append(' ');
		}
		return sb.toString().trim();
	}

	public static String hexStr2Str(String hexStr)
	{
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++)
		{
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}


	/**
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 * @param bytes bytes
	 * @return String
	 */
	public static String decode(String bytes)
	{
		ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2);
		//将每2位16进制整数组装成一个字节
		for(int i=0; i<bytes.length(); i+=2){
			baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1))));
		}
		return new String(baos.toByteArray());
	}

	public static String toStringHex(String s)
	{
		if("0x".equals(s.substring(0, 2)))
		{
			s = s.substring(2);
		}
		byte[] baKeyword = new byte[s.length()/2];
		for(int i = 0; i < baKeyword.length; i++)
		{
			try{
				baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2), 16));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		try
		{
			//UTF-16le:Not
			s = new String(baKeyword, "utf-8");
		}
		catch (Exception e1){
			e1.printStackTrace();
		}
		return s;
	}
}
