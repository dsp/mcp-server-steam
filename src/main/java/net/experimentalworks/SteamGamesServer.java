package net.experimentalworks;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.spec.ServerMcpTransport;
import reactor.core.publisher.Mono;

public class SteamGamesServer {

  private static final String STEAM_API_KEY = System.getenv("STEAM_API_KEY");
  private static final String STEAM_ID = System.getenv("STEAM_ID");
  private final McpAsyncServer server;

  public SteamGamesServer(ServerMcpTransport transport) {
    String version = getClass().getPackage().getImplementationVersion();
    if (version == null) {
      version = "1.0.0"; // Fallback version if not found
    }
    this.server =
        McpServer.async(transport)
            .serverInfo("steam-games", version)
            .capabilities(ServerCapabilities.builder().tools(true).logging().build())
            .build();
  }

  public Mono<Void> run() {
    return server
        .addTool(createGetGamesTool())
        .then(server.addTool(createGetRecentGamesTool()))
        .then(Mono.never());
  }

  private static McpServerFeatures.AsyncToolRegistration createGetGamesTool() {
    var schema =
        """
            {
              "type": "object",
              "properties": {}
            }
            """;

    var tool =
        new Tool(
            "get-games",
            """
            Get a comprehensive list of all games owned by the specified Steam user, including their total playtime in minutes.
            This includes all games in their Steam library, both installed and uninstalled, free and purchased. For each game,
            returns details like the game name, AppID, total playtime (in minutes), and whether they've played it recently. The data comes
            directly from Steam's official API using the provided Steam ID.
            """,
            schema);

    return new McpServerFeatures.AsyncToolRegistration(tool, args -> handleGetGames(args));
  }

  private static Mono<CallToolResult> handleGetGames(Map<String, Object> args) {
    return Mono.fromCallable(
        () -> {
          var steamGames = new SteamGames(STEAM_API_KEY);
          var games = steamGames.getGames(STEAM_ID);

          var json =
              new JSONObject()
                  .put("owner", STEAM_ID)
                  .put("description", "Played games by the given steam id")
                  .put("all_games", new JSONArray(games));

          return new CallToolResult(List.of(new TextContent(json.toString())), false);
        });
  }

  private static McpServerFeatures.AsyncToolRegistration createGetRecentGamesTool() {
    var schema =
        """
            {
              "type": "object",
              "properties": {}
            }
            """;

    var tool =
        new Tool(
            "get-recent-games",
            """
            Retrieve a list of recently played games for the specified Steam user, including playtime
            details from the last 2 weeks (in minutes). This tool fetches data directly from Steam's API using the
            provided Steam ID and returns information like game names, AppIDs, and recent playtime
            statistics in minutes. The results only include games that have been played in the recent time period,
            making it useful for tracking current gaming activity and habits.
            """,
            schema);

    return new McpServerFeatures.AsyncToolRegistration(tool, args -> handleGetRecentGames(args));
  }

  private static Mono<CallToolResult> handleGetRecentGames(Map<String, Object> args) {
    return Mono.fromCallable(
        () -> {
          var steamGames = new SteamGames(STEAM_API_KEY);
          var games = steamGames.getRecentlyGames(STEAM_ID);

          var json =
              new JSONObject()
                  .put("owner", STEAM_ID)
                  .put("description", "Recently played games by the given steam id")
                  .put("recent_games", new JSONArray(games));

          return new CallToolResult(List.of(new TextContent(json.toString())), false);
        });
  }
}
