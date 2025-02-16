{ pkgs, lib, config, ... }: {
  languages.java = {
    enable = true;
    jdk.package = pkgs.jdk;
    maven.enable = true;
  };
  # See full reference at https://devenv.sh/reference/options/
}
