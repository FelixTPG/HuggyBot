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
        System.out.println(petpetURL);
        event.getHook().editOriginal(petpetURL).queue();
    }

}