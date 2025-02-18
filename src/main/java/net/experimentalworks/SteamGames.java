package net.experimentalworks;

import java.util.List;
import java.util.stream.Collectors;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.data.json.recentlyplayedgames.GetRecentlyPlayedGames;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.GetRecentlyPlayedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

public class SteamGames {

  private final SteamWebApiClient client;

  public SteamGames(String apiKey) {
    this.client = new SteamWebApiClient.SteamWebApiClientBuilder(apiKey).build();
  }

  public GetOwnedGames getOwnedGames(String steamId) throws SteamApiException {
    GetOwnedGamesRequest request =
        SteamWebApiRequestFactory.createGetOwnedGamesRequest(steamId, true, true, List.of());
    return client.processRequest(request);
  }

  public GetRecentlyPlayedGames getRecentlyPlayedGames(String steamId) throws SteamApiException {
    GetRecentlyPlayedGamesRequest request =
        SteamWebApiRequestFactory.createGetRecentlyPlayedGamesRequest(steamId);

    return client.processRequest(request);
  }

  public List<Game> getGames(String steamId) throws SteamApiException {
    GetOwnedGames ownedGames = getOwnedGames(steamId);
    return ownedGames.getResponse().getGames().stream()
        .map(game -> new Game(game.getAppid(), game.getName(), game.getPlaytimeForever()))
        .collect(Collectors.toList());
  }

  public List<Game> getRecentlyGames(String steamId) throws SteamApiException {
    GetRecentlyPlayedGames recentGames = getRecentlyPlayedGames(steamId);
    return recentGames.getResponse().getGames().stream()
        .map(
            game ->
                new Game(
                    game.getAppid(),
                    game.getName(),
                    game.getPlaytimeForever(),
                    game.getPlaytime2weeks()))
        .collect(Collectors.toList());
  }
}
