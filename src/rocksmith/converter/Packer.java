package rocksmith.converter;

import com.google.common.collect.ImmutableList;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import rocksmith.converter.Platform.GamePlatform;
import rocksmith.converter.psarc.Psarc;

public class Packer {

  public static Path unpack(
      Path srcPath,
      Path destDirPath,
      Platform predefinedPlatform,
      boolean decodeAudio,
      boolean overwriteSongXml) throws Exception {

    // Only support PC -> PS3 conversions
    assert (predefinedPlatform.getPlatform() == GamePlatform.PC);

    try (BufferedInputStream inputStream = new BufferedInputStream(
        Files.newInputStream(srcPath, StandardOpenOption.READ))) {
      return extractPSARC(srcPath, destDirPath, inputStream, predefinedPlatform, true);
    }
  }

  private static Path extractPSARC(
      Path srcPath, Path destPath, InputStream inputStream, Platform platform,
      boolean isInitialCall) throws Exception {
    if (isInitialCall) {
      destPath = getUnpackedDir(srcPath, destPath, platform);
    }

    Psarc psarc = new Psarc();
    psarc.read(inputStream, true);

    return destPath;
  }

  private static Path getUnpackedDir(Path srcPath, Path destPath, Platform platform) {
    String filenameWithoutExtension = srcPath.getFileName().toString();
    ImmutableList<String> extensions = ImmutableList.of(
        "_p.psarc", "_m.psarc", "_ps3.psarc.edat", "_xbox", ".dat"
    );

    for (String extension : extensions) {
      if (filenameWithoutExtension.endsWith(extension)) {
        filenameWithoutExtension = filenameWithoutExtension.replace(extension, "");
        break;
      }
    }

    Path unpackedDir = Paths.get(String.format("%s_%s", filenameWithoutExtension, platform));

    if (destPath != null) {
      unpackedDir = destPath.resolve(unpackedDir);
    }

    return unpackedDir;
  }
}
