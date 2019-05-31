package rocksmith.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DLCPackageConverter {

  public static String convert(
      Path sourcePackage,
      Platform sourcePlatform,
      Platform targetPlatform,
      String appId) throws Exception {

    Packer.unpack(
        sourcePackage, Files.createTempDirectory("rsConvert"), sourcePlatform, false, true);
    return "";
  }

}
