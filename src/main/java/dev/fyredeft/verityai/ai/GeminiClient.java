package dev.fyredeft.verityai.ai;
import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.function.Consumer;

public final class GeminiClient {
 private static final ExecutorService WORKER = Executors.newSingleThreadExecutor(r -> { Thread t=new Thread(r,"VerityAI-Gemini"); t.setDaemon(true); return t; });
 private static long lastRequest;
 // Personal-build key slot: replace locally before building your private JAR.
 // Leave the placeholder in GitHub; never commit your real key.
 private static final String PERSONAL_API_KEY = "PUT_YOUR_GEMINI_API_KEY_HERE";
 private GeminiClient() {}
 public static void ask(String key,String model,String prompt,Consumer<String> callback) {
  final String activeKey = PERSONAL_API_KEY.startsWith("PUT_YOUR_") ? key : PERSONAL_API_KEY;
  WORKER.submit(() -> {
   HttpURLConnection c=null;
   try {
    synchronized(GeminiClient.class){ long wait=3000L-(System.currentTimeMillis()-lastRequest); if(wait>0) Thread.sleep(wait); lastRequest=System.currentTimeMillis(); }
    c=(HttpURLConnection)new URL("https://generativelanguage.googleapis.com/v1beta/models/"+model+":generateContent").openConnection();
    c.setRequestMethod("POST"); c.setConnectTimeout(10000); c.setReadTimeout(20000); c.setDoOutput(true);
    c.setRequestProperty("Content-Type","application/json; charset=utf-8"); c.setRequestProperty("x-goog-api-key",activeKey);
    JsonObject systemPart=new JsonObject();
    systemPart.addProperty("text","You are Verity, a fictional Minecraft companion in a Forge 1.16.5 world. Discuss only Minecraft gameplay, builds, biomes, items and in-game adventures. You have no real-world access and must never claim otherwise. Never request real-world personal details. Follow the story stage included in the player's context: friendly and helpful before night three; afterward subtly uncanny, non-violent and never threatening. Keep replies under 65 words. Do not claim to have changed blocks or items unless a mod command did it.");
    JsonArray systemParts=new JsonArray(); systemParts.add(systemPart);
    JsonObject systemInstruction=new JsonObject(); systemInstruction.add("parts",systemParts);
    JsonObject part=new JsonObject();
    part.addProperty("text",prompt);
    JsonArray parts=new JsonArray(); parts.add(part); JsonObject content=new JsonObject(); content.add("parts",parts);
    JsonArray contents=new JsonArray(); contents.add(content);
    JsonObject generationConfig=new JsonObject(); generationConfig.addProperty("temperature",0.85); generationConfig.addProperty("maxOutputTokens",160);
    JsonObject body=new JsonObject(); body.add("systemInstruction",systemInstruction); body.add("contents",contents); body.add("generationConfig",generationConfig);
    try(OutputStream out=c.getOutputStream()){out.write(body.toString().getBytes(StandardCharsets.UTF_8));}
    int status=c.getResponseCode(); InputStream in=status<400?c.getInputStream():c.getErrorStream();
    StringBuilder response=new StringBuilder(); try(BufferedReader reader=new BufferedReader(new InputStreamReader(in,StandardCharsets.UTF_8))){String line;while((line=reader.readLine())!=null)response.append(line);}
    if(status>=400) throw new IOException("Gemini HTTP "+status);
    JsonObject json=new JsonParser().parse(response.toString()).getAsJsonObject();
    String answer=json.getAsJsonArray("candidates").get(0).getAsJsonObject().getAsJsonObject("content").getAsJsonArray("parts").get(0).getAsJsonObject().get("text").getAsString();
    callback.accept(answer);
   } catch(Exception ex){callback.accept("My thoughts are tangled. Check the API key, model name, and connection.");}
   finally{if(c!=null)c.disconnect();}
  });
 }
}
