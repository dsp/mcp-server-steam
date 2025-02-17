package net.experimentalworks;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import java.util.List;

public class SteamGames {

    private final SteamWebApiClient client;

    public SteamGames(String apiKey) {
        this.client = new SteamWebApiClient.SteamWebApiClientBuilder(
            apiKey
        ).build();
    }

    public GetOwnedGames getOwnedGames(String steamId)
        throws SteamApiException {
        GetOwnedGamesRequest request =
            new GetOwnedGamesRequest.GetOwnedGamesRequestBuilder(steamId)
                .includeAppInfo(true)
                .includePlayedFreeGames(true)
                .buildRequest();

        return client.processRequest(request);
    }

    public List<Game> getGames(String steamId) throws SteamApiException {
        GetOwnedGames ownedGames = getOwnedGames(steamId);
        return ownedGames.getResponse().getGames();
    }
}
