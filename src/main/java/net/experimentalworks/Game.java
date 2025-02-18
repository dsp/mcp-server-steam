package net.experimentalworks;

import java.io.Serializable;
import java.util.Optional;

public class Game implements Serializable {

    private long appId;
    private String name;
    private float playtimeForever;
    private Optional<Float> playtime2weeks;

    public Game(long appId, String name, float playtimeForever) {
        this.appId = appId;
        this.name = name;
        this.playtimeForever = playtimeForever;
    }

    public Game(
        long appId,
        String name,
        float playtimeForever,
        float playtime2weeks
    ) {
        this.appId = appId;
        this.name = name;
        this.playtimeForever = playtimeForever;
        this.playtime2weeks = Optional.of(playtime2weeks);
    }

    public long getAppId() {
        return appId;
    }

    public String getName() {
        return name;
    }

    public float getPlaytimeForever() {
        return playtimeForever;
    }

    public Optional<Float> getPlaytime2weeks() {
        return playtime2weeks;
    }
}
