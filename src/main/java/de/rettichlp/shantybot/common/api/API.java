package de.rettichlp.shantybot.common.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static com.google.gson.JsonParser.parseString;
import static de.rettichlp.shantybot.ShantyBot.discordLogging;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;

@Log4j2
public class API {

    /**
     * Checks if the API is online
     *
     * @return true if the API is online, false otherwise
     *
     * @see #getJsonObject()
     */
    public boolean apiNotReachable() {
        return isNull(getJsonObject());
    }

    /**
     * Returns the version of the server
     *
     * @return the version of the server
     *
     * @see #getJsonObject()
     */
    @Nullable
    public String getVersion() {
        return ofNullable(getJsonObject())
                .map(jsonObject -> jsonObject.get("version"))
                .map(JsonElement::getAsString)
                .orElse(null);
    }

    /**
     * Checks if the server is in maintenance mode by checking if the MOTD contains the word "Wartungsarbeiten"
     *
     * @return true if the server is in maintenance mode, false otherwise
     *
     * @see #getJsonObject()
     */
    public boolean isMaintenance() {
        return ofNullable(getJsonObject())
                .map(jsonObject -> jsonObject.get("motd"))
                .map(JsonElement::getAsJsonObject)
                .map(jsonObject -> jsonObject.get("clean"))
                .map(jsonElement -> jsonElement.getAsString().toLowerCase().contains("wartungsarbeiten"))
                .orElse(false);
    }

    /**
     * Checks if the server is offline
     *
     * @return true if the server is offline, false otherwise
     *
     * @see #getJsonObject()
     */
    @Nullable
    public Boolean isOffline() {
        return ofNullable(getJsonObject())
                .map(jsonObject -> !jsonObject.get("online").getAsBoolean())
                .orElse(null);
    }

    /**
     * Returns the amount of online players
     *
     * @return the amount of online players
     *
     * @see #getPlayerJsonObject()
     */
    public int getOnlinePlayers() {
        return ofNullable(getPlayerJsonObject())
                .map(jsonObject -> jsonObject.get("online").getAsInt())
                .orElse(0);
    }

    /**
     * Returns the maximum amount of players
     *
     * @return the maximum amount of players or null if the player JSON object is null
     *
     * @see #getPlayerJsonObject()
     */
    @Nullable
    public Integer getMaxPlayers() {
        JsonObject playerJsonObject = getPlayerJsonObject();
        return isNull(playerJsonObject) ? null : playerJsonObject.get("max").getAsInt();
    }

    /**
     * Returns the player object from the JSON response
     *
     * @return the player object from the JSON response or null if the JSON object is null
     *
     * @see #getJsonObject()
     */
    @Nullable
    private JsonObject getPlayerJsonObject() {
        return ofNullable(getJsonObject())
                .map(jsonObject -> jsonObject.get("players"))
                .map(JsonElement::getAsJsonObject)
                .orElse(null);
    }

    /**
     * Returns the JSON object from the API
     *
     * @return the JSON object from the API or null if the response is not successful
     */
    @Nullable
    private JsonObject getJsonObject() {
        ResponseEntity<String> responseEntity = sendRequest();
        return responseEntity.getStatusCode().is2xxSuccessful()
                ? parseString(requireNonNull(responseEntity.getBody())).getAsJsonObject()
                : null;
    }

    /**
     * Sends a request to the API
     *
     * @return the response entity
     */
    private ResponseEntity<String> sendRequest() {
        return WebClient.builder()
                .baseUrl("https://api.mcsrvstat.us/3/shantytown.eu")
                .build()
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    String responseBodyAsString = ex.getResponseBodyAsString();

                    HttpStatusCode statusCode = ex.getStatusCode();
                    if (!statusCode.is4xxClientError()) {
                        log.error("Request failed with code {}: {}", statusCode, responseBodyAsString);
                        discordLogging.error("Request failed with code {}: {}", statusCode, responseBodyAsString);
                    }

                    return just(ResponseEntity.status(statusCode).body(responseBodyAsString));
                })
                .block();
    }
}
