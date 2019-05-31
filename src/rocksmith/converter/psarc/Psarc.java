package rocksmith.converter.psarc;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import rocksmith.converter.Rijndael;

public class Psarc {

  class Header {

    public long magicNumber;
    public long versionNumber;
    public long compressionMethod;
    public long totalTocSize;
    public long tocEntrySize;
    public long numFiles;
    public long blockSizeAlloc;
    public long archiveFlags;

    public Header() {
      magicNumber = 1347633490; //'PSAR'
      versionNumber = 65540; //1.4
      compressionMethod = 2053925218; //'zlib' (also available 'lzma')
      tocEntrySize = 30;//bytes
      //NumFiles = 0;
      blockSizeAlloc = 65536; //Decompression buffer size = 64kb
      archiveFlags = 0; //It's bitfield actually, see PSARC.bt
    }

    @Override
    public String toString() {
      return String.format(
          "magicNumber: %d%nversionNumber: %d%ncompressionMethod: %d%n"
              + "totalTocSize: %d%ntocEntrySize: %d%nnumFiles: %d%n"
              + "blockSizeAlloc: %d%n archiveFlags: %d%n"
          , magicNumber, versionNumber, compressionMethod, totalTocSize, tocEntrySize,
          numFiles, blockSizeAlloc, archiveFlags);
    }
  }

  private Header header;
  private List<Entry> toc;
  public List<Entry> TOC;
  private long[] _zBlocksSizeList;
  private int bNum;
  private DataInputStream reader;

  public Psarc() {
    header = new Header();
    toc = new ArrayList<>();
    toc.add(new Entry());
  }

  public void read(InputStream inputStream, boolean lazy) throws Exception {
    toc.clear();
    reader = new DataInputStream(inputStream);
    header.magicNumber = reader.readInt();
    if (header.magicNumber != 1347633490) {
      throw new IllegalStateException(
          String.format("Magic number is not correct: %d", header.magicNumber));
    }

    header.versionNumber = reader.readInt();
    header.compressionMethod = reader.readInt();
    header.totalTocSize = reader.readInt();
    header.tocEntrySize = reader.readInt();
    header.numFiles = reader.readInt();
    header.blockSizeAlloc = reader.readInt();
    header.archiveFlags = reader.readInt();
    System.out.printf("Header: %n%s%n", header);

    int tocSize = (int) header.totalTocSize - 32;

    if (header.archiveFlags == 4) { //TOC_ENCRYPTED
      ByteArrayOutputStream tocStream = new ByteArrayOutputStream();
      Rijndael.decryptPsarc(reader, tocStream, header.totalTocSize);
    }
  }

  public int getbNum() {
    return (int) log(header.blockSizeAlloc, 256); // 256 = max byte value + 1
  }

  private static double log(double value, double base) {
    return Math.log(value) / Math.log(base);
  }
}
