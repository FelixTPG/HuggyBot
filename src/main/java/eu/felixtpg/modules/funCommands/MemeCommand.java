package eu.felixtpg.modules.funCommands;

import com.fasterxml.jackson.core.JsonParser;
import eu.felixtpg.Main;
import eu.felixtpg.api.commands.Command;
import eu.felixtpg.api.commands.CommandRuntime;
import eu.felixtpg.api.exceptions.CommandException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Command(name = "meme", emoji = "🤣", guildOnly = false)
public class MemeCommand extends CommandRuntime {

    @Override
    public SlashCommandData initCommand() {
        return Commands.slash("meme", "Get a random meme from Reddit, simply thrown into your face!")
                .setDescriptionLocalization(DiscordLocale.GERMAN, "Hole dir ein zufälliges Meme von Reddit, einfach so ins Gesicht geworfen!");
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws CommandException {
        event.deferReply().queue();

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://meme-api.com/gimme"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());

            EmbedBuilder builder = Main.getNewEmbed()
                    .setDescription("## 🚂 - Random Reddit Meme\n" +
                            "**Title:** [" + json.getString("title") + "](" + json.getString("postLink") + ")\n" +
                            "**Author:** " + json.getString("author") + " on r/" + json.getString("subreddit") + "\n")
                    .setImage(json.getString("url"));

            event.getHook().editOriginalEmbeds(builder.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException("An error occurred while fetching the meme from Reddit!");
        }
    }

}
