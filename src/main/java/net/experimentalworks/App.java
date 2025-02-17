package net.experimentalworks;

import io.modelcontextprotocol.server.transport.StdioServerTransport;

/** Hello world! */
public class App {

  public static void main(String[] args) {
    var server = new SteamGamesServer(new StdioServerTransport());
    server.run().block();
  }
}
