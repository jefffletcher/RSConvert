package rocksmith.converter.psarc;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Entry {

  public byte[] MD5;
  public long zIndexBegin;
  public long length;
  public long offset;
  public InputStream data;
  public int id;
  public boolean isCompressed;
  public String name;

  public Entry() {
    this.id = 0;
    this.name = "";
  }

  public void updateNameMd5() throws NoSuchAlgorithmException {
    if (name.isEmpty()) {
      MD5 = new byte[16];
    } else {
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      MD5 = messageDigest.digest(name.getBytes());
    }
  }

  @Override
  public String toString() {
    return name;
  }
}
