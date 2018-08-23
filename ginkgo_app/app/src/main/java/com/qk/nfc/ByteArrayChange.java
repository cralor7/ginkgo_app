package com.qk.nfc;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 * ByteArrayChange
 */
public class ByteArrayChange {
	
	//ת����1   ��ʽΪ0xabcd1234  ��ĸСд
	/* public static  String ByteArrayToHexString(byte[] src) {
	      StringBuilder stringBuilder = new StringBuilder("0x");
	      if (src == null || src.length <= 0) {
	          return null;
	      }
	      char[] buffer = new char[2];
	      for (int i = 0; i < src.length; i++) {
	          buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
	          buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
	          System.out.println(buffer);
	          stringBuilder.append(buffer);
	      }
	      return stringBuilder.toString();
	  }*/
	  
	  //ת����2   ��ʽΪ  ABCD1234 ��ĸ��д
	public static  String ByteArrayToHexString(byte[] bytesId) {   //Byte����ת��Ϊ16�����ַ���
				// TODO �Զ����ɵķ������
			 int i, j, in;
		        String[] hex = {
		                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"
		        };
		        String output = "";

		        for (j = 0; j < bytesId.length; ++j) {
		            in = bytesId[j] & 0xff;
		            i = (in >> 4) & 0x0f;
		            output += hex[i];
		            i = in & 0x0f;
		            output += hex[i];
		        }
		        return output;
			}
}
