package eu.felixtpg.modules.funCommands;

import eu.felixtpg.api.commands.Command;
import eu.felixtpg.api.commands.CommandRuntime;
import eu.felixtpg.api.exceptions.CommandException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

@Command(name = "deadchat", emoji = "💀", guildOnly = false)
public class DeadChatCommand extends CommandRuntime {

    @Override
    public SlashCommandData initCommand() {
        return Commands.slash("deadchat", "When the chat is dead, use this command!")
                .setDescriptionLocalization(DiscordLocale.GERMAN, "Wenn der Chat tot ist, benutze diesen Command!");
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws CommandException {
        event.reply("https://c.tenor.com/71DeLT3bO0AAAAAd/tenor.gif").queue();
    }

}
