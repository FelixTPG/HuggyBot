package eu.felixtpg;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class TextCommands extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        final Message message = event.getMessage();
        final String content = message.getContentRaw();

        if (message.getAuthor().isBot() || !content.startsWith(Main.PREFIX)) {
            return;
        }

        String[] args = content.substring(Main.PREFIX.length()).split(" ");
        String command = args[0];
        List<String> arguments = Arrays.asList(args).subList(1, args.length);

        Main.getLogger().info(event.getAuthor().getName() + " (ID - " + event.getAuthor().getId() +  ") used command " + command);

        switch (command) {
            case "hug" -> {
                // get target
                Member target = message.getMentions().getMembers().get(0);
                if (target == null) {
                    message.reply("I cannot hug someone if they're gone... :(").queue();
                    return;
                }

                message.replyEmbeds(
                        Main.getNewEmbed()
                                .setDescription("")
                                .build()
                ).queue();
            }
            case "ping" -> message.reply("Heya! How are u doing? <:huggy:1279480028295467188>").queue();
            case "embed" -> {
                if (!event.getMember().hasPermission(net.dv8tion.jda.api.Permission.MESSAGE_MANAGE)) {
                    event.getMessage().reply("You do not have permission to use this command!").queue();
                    return;
                }
                EmbedBuilder embed = new EmbedBuilder()
                        .setDescription(String.join(" ", arguments))
                        .setColor(Color.BLUE);
                event.getChannel().sendMessageEmbeds(embed.build()).queue();
            }
            case "stop" -> {
                if (event.getAuthor().getIdLong() != 932680842466181201L) {
                    event.getMessage().reply("You do not have permission to use this command!").queue();
                    return;
                }

                event.getMessage().addReaction(Emoji.fromUnicode("U+1F44B")).queue();
                event.getJDA().shutdown();
                System.exit(0);
            }
            case "help" -> {
                message.reply("This is currently not available!").queue();
            }
            default -> event.getMessage().reply("Command not found! Try " + Main.PREFIX + "help!").queue();
        }
    }

}