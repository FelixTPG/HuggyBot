package eu.felixtpg.api.commands;

import eu.felixtpg.Main;
import eu.felixtpg.Var;
import eu.felixtpg.api.exceptions.CommandException;
import eu.felixtpg.utils.DeMoji;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Runner extends ListenerAdapter {

    private static final List<BotCommand> commands = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    public Runner() {
        Main.getJda().addEventListener(this);
    }

    public static MessageEmbed getErrorEmbed(Exception exe) {
        EmbedBuilder builder = Main.getNewEmbed()
                .setColor(new Color(255, 0, 0))
                .setDescription("› **" + DeMoji.X.getV() + " - Error:** An error occurred while executing the command!\n" +
                        "```" + exe.getMessage() + "```")
                .addField(DeMoji.INFO.getV() + " - Do you have a problem?", "Contact us on our [support discord](" + Var.SUPPORT_SERVER_LINK + ")!", false);

        if (exe instanceof CommandException cmdExe) {
                builder.setDescription("› **" + DeMoji.X.getV() + " - Error:** An error occurred while executing the command!\n" +
                        "```" + exe.getMessage() + "```");
        }


        builder.setDescription("› **" + DeMoji.X.getV() + " - Error:** An **system** error occurred while executing the command!\n" +
                "```" + exe.getMessage() + "```");

        return builder.build();
    }

    public static BotCommand getBotCommand(String name) {
        return commands.stream().filter(botCommand -> botCommand.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static void addCommand(BotCommand botCommand) {
        commands.add(botCommand);
        Main.getLogger().info("{} has been registered!", botCommand.getName());
    }

    public void registerCommands() {
        // Verwende Reflections, um alle Klassen im Package zu finden
        Reflections reflections = new Reflections("eu.felixtpg");

        // Finde alle Klassen, die mit @Command annotiert sind
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Command.class);

        for (Class<?> annotatedClass : annotatedClasses) {
            // Überprüfen, ob die Klasse von CommandRuntime erbt
            if (CommandRuntime.class.isAssignableFrom(annotatedClass)) {
                @SuppressWarnings("unchecked")
                Class<? extends CommandRuntime> commandClass = (Class<? extends CommandRuntime>) annotatedClass;
                Command commandAnnotation = commandClass.getAnnotation(Command.class);

                String name = commandAnnotation.name();
                String emoji = commandAnnotation.emoji();

                // Überprüfung auf Duplikate
                if (commands.stream().anyMatch(botCommand -> botCommand.getName().equalsIgnoreCase(name))) {
                    logger.error("Command '" + name + "' is already registered. Skipping duplicate.");
                    continue;
                }

                try {
                    // Eine neue Instanz der Klasse erstellen
                    CommandRuntime commandInstance = commandClass.getDeclaredConstructor().newInstance();

                    // Den neuen BotCommand erstellen
                    new BotCommand(name, emoji, commandInstance, commandAnnotation.guildOnly());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Warnung ausgeben, wenn die Klasse nicht von CommandRuntime erbt
                logger.error("Class '" + annotatedClass.getName() + "' is annotated with @Command but does not extend CommandRuntime. Skipping.");
            }
        }
    }

    public void updateCommands() {
        //LogManager.sendLog(Runner.class, "SETUP", "Initializing slash commands...");

        registerCommands();

        List<CommandData> finalCommandList = new ArrayList<>();
        commands.forEach(botCommand -> {
            // define slash command data and set the name
            SlashCommandData finalCommand = botCommand.getCommand();
            if (finalCommand == null) return;
            finalCommand.setName(botCommand.getName()).setGuildOnly(botCommand.isGuildOnly());

            // define the separators that should be added to the command descriptions
            final String descriptionSeparator = botCommand.getEmoji() + " - ";
            final String optionSeparator = botCommand.getEmoji() + " × ";

            // UPDATE COMMAND DESCRIPTION (ALL LANGUAGES)
            finalCommand.setDescription(descriptionSeparator + finalCommand.getDescription());
            finalCommand.getDescriptionLocalizations().toMap().forEach((discordLocale, s) -> finalCommand.getDescriptionLocalizations().setTranslation(discordLocale, descriptionSeparator + s));

            // UPDATE SUBCOMMAND DESCRIPTION (ALL LANGUAGES)
            finalCommand.getSubcommands().forEach(subcommandData -> {
                subcommandData.setDescription(descriptionSeparator + subcommandData.getDescription());
            });
            finalCommand.getSubcommands().forEach(subcommandData -> {
                subcommandData.getDescriptionLocalizations().toMap().forEach((discordLocale, s) ->
                        subcommandData.getDescriptionLocalizations().setTranslation(discordLocale, descriptionSeparator + s));
            });

            // UPDATE OPTION DESCRIPTIONS FROM COMMAND AND SUBCOMMANDS
            finalCommand.getOptions().forEach(optionData -> optionData.setDescription(optionSeparator + optionData.getDescription()));
            finalCommand.getSubcommands().forEach(subcommandData -> {
                subcommandData.getOptions().forEach(optionData -> optionData.setDescription(optionSeparator + optionData.getDescription()));
            });

            // UPDATE OPTION DESCRIPTIONS FROM COMMAND AND SUBCOMMANDS IN OTHER LANGUAGES
            finalCommand.getOptions().forEach(optionData -> {
                optionData.getDescriptionLocalizations().toMap().forEach((discordLocale, s) -> {
                    optionData.setDescriptionLocalization(discordLocale, optionSeparator + s);
                });
            });
            finalCommand.getSubcommands().forEach(subcommandData -> {
                subcommandData.getOptions().forEach(optionData -> optionData.getDescriptionLocalizations().toMap().forEach((discordLocale, s) -> {
                    optionData.setDescriptionLocalization(discordLocale, optionSeparator + s);
                }));
            });

            // SAME AGAIN FOR SUBCOMMAND GROUPS

            // UPDATE COMMAND DESCRIPTION (ALL LANGUAGES)
            finalCommand.getSubcommandGroups().forEach(subcommandGroupData -> {
                subcommandGroupData.setDescription(descriptionSeparator + finalCommand.getDescription());

                subcommandGroupData.getSubcommands().forEach(subcommandData -> {
                    subcommandData.setDescription(descriptionSeparator + subcommandData.getDescription());

                    subcommandGroupData.getDescriptionLocalizations().toMap().forEach((discordLocale, s) ->
                            subcommandData.getDescriptionLocalizations().setTranslation(discordLocale, descriptionSeparator + s));

                    // OPTION NAMES
                    subcommandData.getOptions().forEach(optionData -> optionData.setDescription(optionSeparator + optionData.getDescription()));

                    subcommandData.getOptions().forEach(optionData -> {
                        optionData.getDescriptionLocalizations().toMap().forEach((discordLocale, s) -> {
                            optionData.setDescriptionLocalization(discordLocale, optionSeparator + s);
                        });
                    });

                });
            });

            // add final updated command to the list
            finalCommandList.add(finalCommand);
        });

        // update actual bot commands
        Main.getJda().updateCommands().addCommands(finalCommandList).queue();
    }

    // handling
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.isGlobalCommand()) return;

        final User user = event.getUser();
        final BotCommand botCommand = getBotCommand(event.getName());

        // Check if the command is registered
        if (botCommand == null) {
            event.replyEmbeds(Main.getNewEmbed()
                    .setColor(new Color(255, 0, 0))
                    .setDescription("› **" + DeMoji.X.getV() + " - Error:** An error occurred while executing the command!\n" +
                            "```This command isn't registered yet! This is the developer's fault!```")
                    .build()).setEphemeral(true).queue();
            return;
        }

        // Check if the command is enabled
        if (botCommand.isDisabled()) {
            event.replyEmbeds(Main.getNewEmbed()
                    .setColor(new Color(255, 0, 0))
                    .setDescription("› **" + DeMoji.X.getV() + " - Error:** This command got disabled by the developer!")
                    .build()).setEphemeral(true).queue();
            return;
        }

//        if (event.isFromGuild()) {
//            LogManager.sendLog(Runner.class, "[`" + Objects.requireNonNull(event.getGuild()).getName() + "`] `" + event.getUser().getEffectiveName() + "` issued the slash command `" + event.getFullCommandName() + "`");
//        } else {
//            LogManager.sendLog(Runner.class, "`" + event.getUser().getEffectiveName() + "` issued the slash command `" + event.getFullCommandName() + "`");
//        }

        try {
            botCommand.getCommandRuntime().run(event);
        } catch (Exception exe) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exe.printStackTrace(pw);

            if (!(exe instanceof CommandException || exe.getCause() instanceof AssertionError || exe instanceof InsufficientPermissionException || exe instanceof HierarchyException)) {
                // Throw unhandled exception so that it can be caught by the global exception handler
                throw new RuntimeException(exe);
            }

            if (event.isAcknowledged()) {
                event.getHook().editOriginal("").setEmbeds(getErrorEmbed(exe)).queue();
            } else
                event.replyEmbeds(getErrorEmbed(exe)).queue();
        }
    }

}