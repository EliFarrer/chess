package result;

import model.GameMetaData;

import java.util.ArrayList;

public record ListGamesResult(ArrayList<GameMetaData> games) {
}
