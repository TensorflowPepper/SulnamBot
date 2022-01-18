package io.github.tensorflowpepper.sulmc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Command extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getName().equalsIgnoreCase("servercheck")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("서버 상태");
            embed.addField(new MessageEmbed.Field("계폐 여부", "", false));
            embed.addField(new MessageEmbed.Field("플레이어 수", "", false));
            embed.addField(new MessageEmbed.Field("", "", false));
        }
    }
}
