package net.experimentalworks;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.spec.ServerMcpTransport;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;

public class SteamGamesServer {

    private static final String STEAM_API_KEY = System.getenv("STEAM_API_KEY");
    private static final String STEAM_ID = System.getenv("STEAM_ID");
    private final McpAsyncServer server;
    private final SteamGames steamGames;

    public SteamGamesServer(ServerMcpTransport transport) {
        this.server = McpServer.async(transport)
            .serverInfo("steam-games", "1.0.0")
            .capabilities(
                ServerCapabilities.builder().tools(true).logging().build()
            )
            .build();
        this.steamGames = new SteamGames(STEAM_API_KEY);
    }

    public Mono<Void> run() {
        return server.addTool(createGetGamesTool()).then(Mono.never());
    }

    private static McpServerFeatures.AsyncToolRegistration createGetGamesTool() {
        var schema =
            """
            {
              "type": "object",
              "properties": {}
            }
            """;

        var tool = new Tool(
            "get-games",
            "Get list of games and playtime for a Steam user",
            schema
        );

        return new McpServerFeatures.AsyncToolRegistration(tool, args ->
            handleGetGames(args)
        );
    }

    private static Mono<CallToolResult> handleGetGames(
        Map<String, Object> args
    ) {
        return Mono.fromCallable(() -> {
            List<GameInfo> games = fetchUserGames(STEAM_ID);

            StringBuilder response = new StringBuilder();
            response
                .append("Games owned by Steam ID ")
                .append(STEAM_ID)
                .append(":\n\n");

            for (GameInfo game : games) {
                double hoursPlayed = game.playtimeMinutes / 60.0;
                response.append(
                    String.format("- %s: %.1f hours\n", game.name, hoursPlayed)
                );
            }

            return new CallToolResult(
                List.of(new TextContent(response.toString())),
                false
            );
        });
    }

    private static record GameInfo(String name, float playtimeMinutes) {}

    private static List<GameInfo> fetchUserGames(String steamId) {
        try {
            SteamGames steamGames = new SteamGames(STEAM_API_KEY);
            return steamGames
                .getGames(steamId)
                .stream()
                .map(game ->
                    new GameInfo(game.getName(), game.getPlaytimeForever())
                )
                .collect(Collectors.toList());
        } catch (SteamApiException e) {
            throw new RuntimeException("Failed to fetch Steam games", e);
        }
    }
}
