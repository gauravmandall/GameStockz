package com.gamestockz.data.modals;

public class GameSectionResult {
    String gameId, result;

    public GameSectionResult() {}

    public GameSectionResult(String gameId, String result) {
        this.gameId = gameId;
        this.result = result;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
