package eu.felixtpg.api.commands;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

@Getter
@Data
public class BotCommand {

    private String name;
    private String emoji;
    private String description;

    @Setter @Getter
    private boolean disabled;

    private boolean guildOnly;
    private SlashCommandData command;
    private CommandRuntime commandRuntime;

    public BotCommand(String name, String emoji, CommandRuntime commandRuntime, boolean guildOnly) {
        this.name = name;
        this.emoji = emoji;

        try {
            this.command = commandRuntime.initCommand();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        this.command.setName(this.name);

//        if (guildOnly) {
//            System.out.println("Guild only command: " + this.name);
//            this.command.setIntegrationTypes(IntegrationType.ALL)
//                    .setContexts(InteractionContextType.GUILD);
//        } else {
            System.out.println("Global command: " + this.name);
            this.command.setIntegrationTypes(IntegrationType.ALL)
                    .setContexts(InteractionContextType.ALL);
//        }

        this.commandRuntime = commandRuntime;
        Runner.addCommand(this);
    }

}
