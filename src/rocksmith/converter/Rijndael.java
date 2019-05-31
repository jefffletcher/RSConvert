package rocksmith.converter;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * https://stackoverflow.com/questions/25064529/decrypt-c-sharp-rijndael-encoded-text
 * https://gist.github.com/dweymouth/11089238
 * https://stackoverflow.com/questions/587357/rijndael-support-in-java
 *
 */
public class Rijndael {

  // private static String CIPHER_SPEC = "AES/CBC/PKCS5Padding";
  // private static String CIPHER_SPEC = "AES/CFB/PKCS5Padding";
  private static String CIPHER_SPEC = "AES/CFB/NoPadding";

  private static byte[] PSARC_KEY = toBytes(new int[]{
      0xC5, 0x3D, 0xB2, 0x38, 0x70, 0xA1, 0xA2, 0xF7,
      0x1C, 0xAE, 0x64, 0x06, 0x1F, 0xDD, 0x0E, 0x11,
      0x57, 0x30, 0x9D, 0xC8, 0x52, 0x04, 0xD4, 0xC5,
      0xBF, 0xDF, 0x25, 0x09, 0x0D, 0xF2, 0x57, 0x2C
  });

  /*
              rij.Padding = PaddingMode.None;
            rij.Mode = cipher;
            rij.BlockSize = 128;
            rij.IV = new byte[16];
            rij.Key = key;          // byte[32]
   */

  public static void decryptPsarc(InputStream input, OutputStream output, long len)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
      InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {

    InputStream encryptedStream = ByteStreams.limit(input, len);

    Cipher cipher = Cipher.getInstance(CIPHER_SPEC);
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(PSARC_KEY, "AES"),
        new IvParameterSpec(new byte[16]));

    byte[] buffer = new byte[512];
    int pad = buffer.length - (int) (len % buffer.length);
    int numRead;
    byte[] decrypted = null;
    while ((numRead = encryptedStream.read(buffer)) > 0) {
      decrypted = cipher.update(buffer, 0, numRead);
      if (decrypted != null) {
        output.write(decrypted);
      }
    }
    if (pad > 0) {
      cipher.update(new byte[pad], 0, pad);
    }
    cipher.doFinal();
    if (decrypted != null) {
      output.write(decrypted);
    }
  }

  private static byte[] toBytes(int[] x) {
    byte[] y = new byte[x.length];
    for (int i = 0; i < x.length; i++) {
      y[i] = (byte) x[i];
    }
    return y;
  }
}
