package eu.felixtpg.modules.funCommands;

import eu.felixtpg.Main;
import eu.felixtpg.api.commands.Command;
import eu.felixtpg.api.commands.CommandRuntime;
import eu.felixtpg.api.exceptions.CommandException;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Command(name = "dab", emoji = "🕺", guildOnly = false)
public class DabCommand extends CommandRuntime {

    @Override
    public SlashCommandData initCommand() {
        return Commands.slash("dab", "Dab on them haters!");
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws CommandException {
        final User user = event.getUser();

        Random random = new Random();

        event.replyEmbeds(
                Main.getNewEmbed(event.getGuild())
                        .setDescription("**" +
                                comment.get(random.nextInt(0, comment.size()))
                                        .replace("$s", user.getAsMention())
                                        .replace("$a", user.getAsMention()) + "**"
                        )
                        .setImage(
                                pictures.get(random.nextInt(0, pictures.size()))
                        )
                        .build()
        ).queue();
    }

    List<String> comment = Arrays.asList(
            "$s just dabbed on them haters!",
            "$s just hit a mighty dab!",
            "$s just hit a dab so hard, it broke the sound barrier!",
            "$s just dabbed on them haters with the power of a thousand suns!",
            "$a just dabbled in the art of dabbing!",
            "$s just unleashed the ultimate dab of destiny!",
            "$s dabbed with the force of a thousand memes!",
            "$s just dropped the dab hammer of justice!",
            "$s dabbed so hard, the server is shaking!",
            "$s just summoned a storm of dabs!",
            "$s’s dab just made history!",
            "$s dabbed with the precision of a pro!",
            "$s’s dab just broke the internet!",
            "$s just hit a dab so epic, it echoed across the galaxy!",
            "$s dabbed and the haters evaporated!",
            "$s's dab was so smooth, it could butter toast!",
            "$s just performed a dab that defies the laws of physics!",
            "$s’s dab just caused a rift in the space-time continuum!",
            "$s just dropped a dab so fierce, it became a legend!",
            "$s's dab was so strong, it leveled up the entire server!",
            "$s dabbed with such swag, everyone else just gave up!",
            "$s’s dab was a masterpiece of meme culture!",
            "$s just dabbed and the sun shone a little brighter!",
            "$s dabbed like there was no tomorrow!",
            "$s’s dab is the stuff of legends, passed down through generations!"
    );

    List<String> pictures = Arrays.asList(
            "https://i.pinimg.com/originals/49/6b/9e/496b9ed21d8bbb1a80030084a4c451ed.gif",
            "https://cdn.felixtpg.eu/huggybot/dab/kids-dab.gif",
            "https://i.giphy.com/3oEjI7M0cOXG0j4HWU.webp",
            "https://media.tenor.com/8XQOdsdty4MAAAAM/dab-betty-white.gif",
            "https://i.pinimg.com/originals/57/80/51/5780510f09bfed93a4d7e779260b0cc5.gif",
            "https://64.media.tumblr.com/85d88d04cdb4a46f6bfbdce34e80e357/tumblr_pjvtxi09Ik1w0433po1_400.gif",
            "https://reactiongifs.me/wp-content/uploads/2021/04/Dab-Dancing.gif",
            "https://i.pinimg.com/originals/31/e9/ea/31e9ea5a4488d3ed0e10f06298561c28.gif",
            "https://i.pinimg.com/originals/9b/21/47/9b2147e6ad5a8c7f0ae0f39d37230a56.gif",
            "https://media.tenor.com/cscqMnXoXJsAAAAi/dab-dance.gif",
            "https://media.tenor.com/7MK5Pqh5VzkAAAAi/pepedab-dab.gif",
            "https://i.giphy.com/B5ODEeRsgYVBPVe2iP.webp",
            "https://gifdb.com/images/high/cartoon-avocado-dab-me0upshf4vb2zudb.gif"
    );

}
