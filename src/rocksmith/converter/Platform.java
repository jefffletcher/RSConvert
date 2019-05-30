package rocksmith.converter;

public class Platform {

  enum GamePlatform {
    PC, MAC, XBOX, PS3
  }

  enum GameVersion {
    RS2012, RS2014
  }

  private final GamePlatform gamePlatform;
  private final GameVersion gameVersion;

  public Platform(GamePlatform gamePlatform, GameVersion gameVersion) {
    this.gamePlatform = gamePlatform;
    this.gameVersion = gameVersion;
  }

  public Platform(String platform, String version) {
    this(GamePlatform.valueOf(platform), GameVersion.valueOf(version));
  }

  public GamePlatform getPlatform() {
    return gamePlatform;
  }

  public GameVersion getVersion() {
    return gameVersion;
  }

  public boolean isConsole() {
    return (gamePlatform == GamePlatform.XBOX || gamePlatform == GamePlatform.PS3);
  }

  @Override
  public String toString() {
    return String.format("%s_%s", gameVersion, gamePlatform);
  }
}
