package io.github.tensorflowpepper.sulmc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class Command extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Command.class);
    ObjectMapper mapper = new ObjectMapper();

    private boolean isConnectionFinished = false;
    Map<String, Object> jsonMap;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} Connected! \n", event.getJDA().getSelfUser().getAsTag());
    }

    public MessageEmbed getJson() throws IOException {
        String responseData = "";
        String returnData = "";
        URL url = new URL("https://sulnamserverpresence-db.herokuapp.com/serverstatus");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestMethod("GET");

        conn.connect();
        System.out.println("http 요청 방식 : "+"GET");
        System.out.println("http 요청 타입 : "+"application/json");
        System.out.println("http 요청 주소 : "+ url.getHost());

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuffer sb = new StringBuffer();
        while ((responseData = br.readLine()) != null) {
            sb.append(responseData);
        }

        returnData = sb.toString();

        String responseCode = String.valueOf(conn.getResponseCode());
        System.out.println("http 응답 코드 : " + responseCode);
        System.out.println("http 응답 데이터 : " + returnData);

        isConnectionFinished = true;

         jsonMap = mapper.readValue(returnData, new TypeReference<Map<String, Object>>() {});
         System.out.println(jsonMap);

         return setEmbedMessage(jsonMap);
    }

    public MessageEmbed setEmbedMessage(Map<String, Object> jsonMap) {

        String isOpen = "서버가 현재 열려 있지 않습니다!";
        String playerNum = "현재 서버가 오프라인입니다!";
        String playerList = "현재 서버가 오프라인입니다!";

        if ((Boolean) jsonMap.get("open")) {
            isOpen = "서버가 현재 열려 있습니다!";
            String[] player = (String[]) jsonMap.get("players");

            playerNum = player.length + "명이 접속하여 있습니다!";
            playerList = player.toString();
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("서버 상태");
        embed.addField(new MessageEmbed.Field("계폐 여부", isOpen, false));
        embed.addField(new MessageEmbed.Field("플레이어 명단", playerList, false));
        embed.addField(new MessageEmbed.Field("플레이어 수", playerNum, false));
        embed.setColor(Color.ORANGE);
        embed.setThumbnail("https://cdn.discordapp.com/attachments/852873136268640266/858646930681757696/sulnam.png");
        embed.setFooter("Developed by TensorflowPepper | Powered by Leadership | Working in JDA 4.4.0_352");

        return embed.build();
    }

   @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getName().equalsIgnoreCase("checkserver")) {
            event.deferReply().queue();
            try {
                getJson();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MessageEmbed messageEmbed = null;

            try {
                 messageEmbed = getJson();
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (!isConnectionFinished) {
                continue;
            }
            event.getChannel().sendTyping().queue();
            event.getHook().sendMessage("서버 상태입니다.").queue();
            event.getChannel().sendMessage(messageEmbed).queue();
        }
    }
}
