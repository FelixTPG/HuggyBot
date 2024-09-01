package eu.felixtpg.api.commands;

import eu.felixtpg.api.exceptions.CommandException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class CommandRuntime extends ListenerAdapter {

//    protected static final IGuildService guildService = ServiceProvider.getService(IGuildService.class);
//    protected static final ILanguageService languageService = ServiceProvider.getService(ILanguageService.class);
//    protected static final IUserService userService = ServiceProvider.getService(IUserService.class);

    public abstract SlashCommandData initCommand();

    public abstract void run(SlashCommandInteractionEvent event) throws CommandException;

}
