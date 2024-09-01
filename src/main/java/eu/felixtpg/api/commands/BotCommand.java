package eu.felixtpg.api.commands;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

@Getter
@Data
public class BotCommand {

    private String name;
    private String emoji;
    private String description;

    @Setter @Getter
    private boolean disabled;

    private String textCommand;

    private boolean guildOnly;
    private SlashCommandData command;
    private CommandRuntime commandRuntime;

    public BotCommand(String name, String emoji, CommandRuntime commandRuntime) {
        this.name = name;
        this.emoji = emoji;

        try {
            this.command = commandRuntime.initCommand();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        this.command
                .setName(this.name);
//                .setIntegrationTypes(integrationTypes)
//                .setContexts(InteractionContextType.ALL);

        this.commandRuntime = commandRuntime;
        Runner.addCommand(this);
    }

//    public BotCommand(String name, String emoji, CommandRuntime commandRuntime, Collection<IntegrationType> integrationTypes) {
//        this.name = name;
//        this.emoji = emoji;
//
//        this.command = commandRuntime.initCommand();
//        this.command
//                .setName(this.name)
//                .setIntegrationTypes(integrationTypes)
//                .setContexts(InteractionContextType.ALL);
//
//        this.commandRuntime = commandRuntime;
//        Runner.addCommand(this);
//    }
//
//    public SlashCommandData setContexts(InteractionContextType... interactionContextTypes) {
//        return this.command.setContexts(interactionContextTypes);
//    }
//
//    public SlashCommandData setContexts(Collection<InteractionContextType> interactionContextTypes) {
//        return this.command.setContexts(interactionContextTypes);
//    }

    public BotCommand setDescription(String string) {
        this.description = string;
        return this;
    }

    public BotCommand setTextCommand(String textCommand) {
        this.textCommand = textCommand;
        return this;
    }

}
