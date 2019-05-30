package rocksmith.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import rocksmith.converter.Platform.GamePlatform;
import rocksmith.converter.Platform.GameVersion;

public class Converter {

  private static final String APP_ID = "248750";
  private static final Platform SOURCE_PLATFORM = new Platform(GamePlatform.PC, GameVersion.RS2014);
  private static final Platform TARGET_PLATFORM =
      new Platform(GamePlatform.PS3, GameVersion.RS2014);

  private final Path sourcePackage;

  Converter(Path sourcePackage) {
    this.sourcePackage = sourcePackage;
  }

  private void convert() throws IOException {
    String result = DLCPackageConverter
        .convert(sourcePackage, SOURCE_PLATFORM, TARGET_PLATFORM, APP_ID);
    System.out.println(result);
  }

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.out.println("Usage: java rocksmith.converter.Converter <filename>");
      System.exit(0);
    }

    Path inputPath = Paths.get(args[0]);
    if (!Files.exists(inputPath)) {
      System.out.printf("File '%s' does not exist.", inputPath);
      System.exit(0);
    }

    if (!inputPath.toString().endsWith("p.psarc")) {
      System.out.printf("File '%s' does not appear to be PC CDLC.", inputPath);
      System.exit(0);
    }

    Converter converter = new Converter(inputPath);
    converter.convert();
  }
}
