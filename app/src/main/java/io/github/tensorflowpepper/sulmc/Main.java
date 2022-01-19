package io.github.tensorflowpepper.sulmc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {

    private Main() throws LoginException {
        JDA jda = JDABuilder.createDefault("OTMyODUwODQzNTU0ODExOTc0.YeY_DA.OKxtDUBnY7r0ROPzoFgxG-b2Lfw")
                .setActivity(Activity.listening("/checkserver"))
                .setStatus(OnlineStatus.IDLE)
                .addEventListeners(new Command())
                .build();

        jda.upsertCommand("checkserver", "Checks the Server Status.").queue();
    }

    public static void main(String[] args) throws LoginException {
        new Main();
    }
}
