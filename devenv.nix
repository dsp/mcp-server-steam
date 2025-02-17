{ pkgs, lib, config, ... }: {
  name = "mcp-steam";
  languages.java = {
    enable = true;
    jdk.package = pkgs.jdk;
    maven.enable = true;
  };

  enterTest = ''
    mvn compile
    mvn spotless:check
  '';

  # watch local changes and build the project to ./dist
  # processes.build.exec = "${pkgs.watchexec}/bin/watchexec mvn package";
}
