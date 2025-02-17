{ pkgs, lib, config, ... }: {
  name = "mcp-steam";
  languages.java = {
    enable = true;
    jdk.package = pkgs.jdk;
    maven.enable = true;
  };

  # watch local changes and build the project to ./dist
  processes.build.exec = "${pkgs.watchexec}/bin/watchexec mvn package";
  processes.serve.exec = "java -jar target/mcp-steam-1.0-SNAPSHOT.jar";
  containers."prod".name = "mcp-steam";
  containers."prod".startupCommand = config.processes.serve.exec;

  # containers."prod".copyToRoot = ./target;
  # containers."prod".startupCommand = "java -jar /mcp-steam-1.0-SNAPSHOT.jar";
  # See full reference at https://devenv.sh/reference/options/
}
