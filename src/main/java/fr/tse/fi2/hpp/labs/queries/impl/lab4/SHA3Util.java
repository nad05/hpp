package fr.tse.fi2.hpp.labs.queries.impl.lab4;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
import org.bouncycastle.util.encoders.Hex;

public class SHA3Util {
    
    public static int digest(String string, int size, int modulo) {
    	 DigestSHA3 md = new DigestSHA3(size);
         String text = string;
         
         try 
         	     {
         	    	 md.update(text.getBytes("UTF-8"));
         	     }
         	     catch (UnsupportedEncodingException ex) 
         	     {
         	    	 // most unlikely
         	         md.update(text.getBytes());
         	     }
         	     
         	     byte[] digest = md.digest();
         	     return encode(digest, modulo);
    }
    
    
    public static int encode(byte [] bytes, int modulo) {
    	BigInteger bigInt = new BigInteger(1, bytes);
    	BigInteger m = new BigInteger(Integer.toString(modulo));
    	return bigInt.mod(m).intValue();
    }
    
    /*public static void main(String[] args) 
    {
    System.out.println(digest("telecom1", 512, 14378));
    System.out.println(digest("telecom2", 512, 14378));
    }*/
}
