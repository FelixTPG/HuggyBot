package eu.felixtpg.modules.funCommands;

import eu.felixtpg.Main;
import eu.felixtpg.api.commands.Command;
import eu.felixtpg.api.commands.CommandRuntime;
import eu.felixtpg.api.exceptions.CommandException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.security.SecureRandom;

@Command(name = "dice", emoji = "🎲", guildOnly = false)
public class DiceCommand extends CommandRuntime {

    @Override
    public SlashCommandData initCommand() {
        return Commands.slash("dice", "Roll the dice!")
                .setDescriptionLocalization(DiscordLocale.GERMAN, "Schmeiß einen Würfel!");
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws CommandException {
        int dice = (int) new SecureRandom().nextInt(6) + 1;
        event.replyEmbeds(
                        Main.getNewEmbed(event.getGuild())
                                .setDescription("Du hast eine **" + dice + "🎲** gewürfelt!")
                                .setImage("https://api.simplica.dev/image/dice/?number=" + dice)
                                .build()
                ).queue();
    }

}
