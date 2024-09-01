package eu.felixtpg.utils;

import eu.felixtpg.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class Embeds {

    public static MessageEmbed getEmbed(@Nullable EmbedType type, String text) {
        if (type == null) {
            return new EmbedBuilder()
                    .setColor(Main.getDefaultColor())
                    .setDescription(text)
                    .build();
        }
        switch (type) {
            case SUCCESS -> {
                return new EmbedBuilder()
                        .setColor(Colors.GREEN)
                        .setDescription(DeMoji.Y.getV() + " " + text).build();
            }
            case INFO -> {
                return new EmbedBuilder()
                        .setColor(Colors.WHITE)
                        .setDescription(DeMoji.INFO.getV() + " " + text).build();
            }
            case ERROR -> {
                return new EmbedBuilder()
                        .setColor(Colors.RED)
                        .setDescription(DeMoji.X.getV() + " " + text).build();
            }
            case RED -> {
                return new EmbedBuilder()
                        .setColor(Colors.RED)
                        .setDescription(text).build();
            }
            case GREEN -> {
                return new EmbedBuilder()
                        .setColor(Colors.GREEN)
                        .setDescription(text).build();
            }
            default -> {
                return new EmbedBuilder()
                        .setColor(Main.getDefaultColor())
                        .setDescription(text)
                        .build();
            }
        }
    }

    public enum EmbedType {

        SUCCESS,
        INFO,
        ERROR,
        GREEN,
        RED;

    }

}
