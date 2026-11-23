package dev.fyredeft.verityai.ai;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.function.Consumer;

/** Lightweight asynchronous Gemini client. Credentials are read from the in-game Forge config. */
public final class GeminiClient {
    private static final ExecutorService WORKER = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "VerityAI-Gemini");
        t.setDaemon(true);
        return t;
    });
    private static long lastRequest;

    private GeminiClient() {}

    public static void ask(String key, String model, String prompt, Consumer<String> callback) {
        final String activeKey = key == null ? "" : key.trim();
        final String activeModel = (model == null || model.trim().isEmpty())
                ? "gemini-2.5-flash" : model.trim();

        if (activeKey.isEmpty()) {
            callback.accept("Open Mods > Verity AI > Config and add your Gemini API key.");
            return;
        }

        WORKER.submit(() -> {
            HttpURLConnection connection = null;
            try {
                synchronized (GeminiClient.class) {
                    long wait = 3000L - (System.currentTimeMillis() - lastRequest);
                    if (wait > 0L) Thread.sleep(wait);
                    lastRequest = System.currentTimeMillis();
                }

                URL endpoint = new URL("https://generativelanguage.googleapis.com/v1beta/models/"
                        + activeModel + ":generateContent");
                connection = (HttpURLConnection) endpoint.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(20000);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setRequestProperty("x-goog-api-key", activeKey);

                JsonObject systemPart = new JsonObject();
                systemPart.addProperty("text",
                    "You are Verity, a fictional Minecraft companion in a Forge 1.16.5 world. "
                    + "Discuss only Minecraft gameplay, builds, biomes, items and in-game adventures. "
                    + "You have no real-world access and must never claim otherwise. "
                    + "Never request real-world personal details. Follow the story stage in the player's "
                    + "context: friendly and helpful before night three; afterward subtly uncanny, "
                    + "non-violent and never threatening. Keep replies under 65 words. "
                    + "Do not claim to have changed blocks or items unless a mod command did it.");
                JsonArray systemParts = new JsonArray();
                systemParts.add(systemPart);
                JsonObject systemInstruction = new JsonObject();
                systemInstruction.add("parts", systemParts);

                JsonObject promptPart = new JsonObject();
                promptPart.addProperty("text", prompt);
                JsonArray promptParts = new JsonArray();
                promptParts.add(promptPart);
                JsonObject content = new JsonObject();
                content.add("parts", promptParts);
                JsonArray contents = new JsonArray();
                contents.add(content);

                JsonObject generationConfig = new JsonObject();
                generationConfig.addProperty("temperature", 0.85);
                generationConfig.addProperty("maxOutputTokens", 160);

                JsonObject body = new JsonObject();
                body.add("systemInstruction", systemInstruction);
                body.add("contents", contents);
                body.add("generationConfig", generationConfig);

                try (OutputStream output = connection.getOutputStream()) {
                    output.write(body.toString().getBytes(StandardCharsets.UTF_8));
                }

                int responseCode = connection.getResponseCode();
                InputStream stream = responseCode < 400
                        ? connection.getInputStream() : connection.getErrorStream();
                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) response.append(line);
                }

                if (responseCode >= 400) {
                    callback.accept("Gemini returned HTTP " + responseCode
                            + ". Check the key, model name, and network connection.");
                    return;
                }

                JsonObject json = new JsonParser().parse(response.toString()).getAsJsonObject();
                String answer = json.getAsJsonArray("candidates").get(0).getAsJsonObject()
                        .getAsJsonObject("content").getAsJsonArray("parts").get(0)
                        .getAsJsonObject().get("text").getAsString();
                callback.accept(answer);
            } catch (Exception exception) {
                callback.accept("Verity could not reach Gemini. Check the key, model, and connection.");
            } finally {
                if (connection != null) connection.disconnect();
            }
        });
    }
}
