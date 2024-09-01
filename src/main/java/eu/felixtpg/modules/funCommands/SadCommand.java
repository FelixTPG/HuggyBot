package eu.felixtpg.modules.funCommands;

import eu.felixtpg.api.commands.Command;
import eu.felixtpg.api.commands.CommandRuntime;
import eu.felixtpg.api.exceptions.CommandException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

@Command(name = "sad", emoji = "😢", guildOnly = false)
public class SadCommand extends CommandRuntime {

    @Override
    public SlashCommandData initCommand() {
        return Commands.slash("sad", "Show other people that you are sad.");
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws CommandException {
    }

}
