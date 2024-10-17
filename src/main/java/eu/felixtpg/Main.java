package eu.felixtpg;

import eu.felixtpg.api.commands.Runner;
import eu.felixtpg.api.elements.StatusObject;
import eu.felixtpg.events.ThreadCreateEvent;
import eu.felixtpg.modules.quotes.QuoteManager;
import eu.felixtpg.utils.SecretLoader;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    @Getter private static final Color defaultColor = new Color(0, 170, 213);
    public static String PREFIX = "h!";

    @Getter private static JDA jda;
    @Getter private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("Starting Bot right here...");

        jda = JDABuilder.createDefault(SecretLoader.getEnv("TOKEN"))
                .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .setActivity(Activity.playing("🔥 warming up..."))
                .build();

        jda.addEventListener(new ThreadCreateEvent());
        jda.addEventListener(new TextCommands());
        jda.addEventListener(new QuoteManager());

        // registering all commands
        Runner runner = new Runner();
        runner.updateCommands();

        jda.awaitReady();
        logger.info("Started bot with " + jda.getGuilds().size() + " servers (Took " + ManagementFactory.getRuntimeMXBean().getUptime() + "ms)");

        updateActivity();
    }

    private static void updateActivity() {
        ArrayList<StatusObject> activities = new ArrayList<>() {{
            add(new StatusObject(Activity.ActivityType.CUSTOM_STATUS, "🧸 Hug Me!"));
            add(new StatusObject(Activity.ActivityType.PLAYING, "with emotions 🌹"));
            add(new StatusObject(Activity.ActivityType.CUSTOM_STATUS, "❤️ love each other"));
        }};

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                StatusObject statusObject = activities.get(random.nextInt(0, activities.size()));
                jda.getPresence().setActivity(Activity.of(statusObject.getType(), statusObject.getName()));
            }
        }, new Date(), TimeUnit.MINUTES.toMillis(1));
    }

    public static EmbedBuilder getNewEmbed() {
        return new EmbedBuilder()
                .setColor(getDefaultColor())
                .setFooter("Huggy 🚀 • " + Var.VERSION, jda.getSelfUser().getAvatarUrl());
    }

    public static EmbedBuilder getNewEmbed(Guild guild) {
        return getNewEmbed().setColor((guild == null || guild.isDetached()) ? getDefaultColor() : guild.getSelfMember().getColor());
    }

}