package inventory;

import java.util.Locale;
import java.util.Objects;


public final class CharacterProfile {
    private final String characterId;
    private final String characterName;
    private final String gameTitle;

    public CharacterProfile(String characterId, String characterName, String gameTitle) {
        this.characterId = requireText(characterId, "Character ID").toUpperCase(Locale.ROOT);
        this.characterName = requireText(characterName, "Character name");
        this.gameTitle = requireText(gameTitle, "Game title");
    }

    public String getCharacterId() {
        return characterId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CharacterProfile profile)) {
            return false;
        }
        return characterId.equals(profile.characterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId);
    }

    @Override
    public String toString() {
        return characterId + " | " + characterName + " | " + gameTitle;
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null.");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return trimmed;
    }
}
