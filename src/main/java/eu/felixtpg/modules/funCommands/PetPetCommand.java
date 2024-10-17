package eu.felixtpg.modules.funCommands;

import eu.felixtpg.api.commands.Command;
import eu.felixtpg.api.commands.CommandRuntime;
import eu.felixtpg.api.exceptions.CommandException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Command(name = "petpet", emoji = "🐾", guildOnly = false)
public class PetPetCommand extends CommandRuntime {

    @Override
    public SlashCommandData initCommand() {
        return Commands.slash("petpet", "PetPet someone!")
                .setDescriptionLocalization(DiscordLocale.GERMAN, "PetPet jemanden!")
                .addOptions(
                        new OptionData(OptionType.USER, "user", "The user you want to petpet.", true)
                                .setDescriptionLocalization(DiscordLocale.GERMAN, "Der Benutzer, den du petten möchtest.")
                );
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws CommandException {
        // EXAMPLE - https://memeado.vercel.app/api/petpet?image=URL
        event.deferReply().queue();

        // get petpet gif and check if it isn't null
        String petpetURL = "https://memeado.vercel.app/api/petpet?image=" + event.getOption("user").getAsUser().getEffectiveAvatarUrl();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(petpetURL))
                .build();

        byte[] petpetGif = null;

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response != null || response.statusCode() == 200) {
                petpetGif = response.body().getBytes();
            }
        } catch (IOException | InterruptedException e) {
            throw new CommandException("An error occurred while fetching the petpet gif. Most likely the user's avatar is invalid.");
        }

        // Try uploading the gif and the user's avatar
        FileUpload upload = FileUpload.fromData(petpetGif, "petpet.gif");
        event.getHook().editOriginalAttachments(
                upload
        ).queue();
    }

}