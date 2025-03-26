package result;

import model.GameData;
import model.GameMetaData;

import java.util.ArrayList;

public record ListGamesResult(ArrayList<GameData> games) {
}
