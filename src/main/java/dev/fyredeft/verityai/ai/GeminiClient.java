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
 private GeminiClient() {}
 public static void ask(String key,String model,String prompt,Consumer<String> callback) {
  WORKER.submit(() -> {
   HttpURLConnection c=null;
   try {
    synchronized(GeminiClient.class){ long wait=3000L-(System.currentTimeMillis()-lastRequest); if(wait>0) Thread.sleep(wait); lastRequest=System.currentTimeMillis(); }
    c=(HttpURLConnection)new URL("https://generativelanguage.googleapis.com/v1beta/models/"+model+":generateContent").openConnection();
    c.setRequestMethod("POST"); c.setConnectTimeout(10000); c.setReadTimeout(20000); c.setDoOutput(true);
    c.setRequestProperty("Content-Type","application/json; charset=utf-8"); c.setRequestProperty("x-goog-api-key",key);
    JsonObject part=new JsonObject();
    part.addProperty("text","You are Verity, a fictional Minecraft companion. Talk only about the Minecraft world and in-game adventures. Never claim real-world access or reveal real-world personal information. Start friendly and become subtly uncanny only when the story stage asks. Keep replies under 65 words. Player: "+prompt);
    JsonArray parts=new JsonArray(); parts.add(part); JsonObject content=new JsonObject(); content.add("parts",parts);
    JsonArray contents=new JsonArray(); contents.add(content); JsonObject body=new JsonObject(); body.add("contents",contents);
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
