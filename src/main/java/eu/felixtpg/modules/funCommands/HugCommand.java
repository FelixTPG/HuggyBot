package eu.felixtpg.modules.funCommands;

import eu.felixtpg.Main;
import eu.felixtpg.api.commands.Command;
import eu.felixtpg.api.commands.CommandRuntime;
import eu.felixtpg.api.elements.CooldownMap;
import eu.felixtpg.api.exceptions.CommandException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Command(name = "hug", emoji = "🧸", guildOnly = false)
public class HugCommand extends CommandRuntime {

    private final CooldownMap cooldown = new CooldownMap(TimeUnit.SECONDS.toMillis(60));

    @Override
    public SlashCommandData initCommand() {
        return Commands.slash("hug", "Hug someone you really like!").setGuildOnly(true)
                .addOptions(
                        new OptionData(OptionType.USER, "user", "The user that you want to hug!", true)
                );
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws CommandException {
        final Member sender = event.getMember();
        final Member target = event.getOption("user", OptionMapping::getAsMember);
        if (target == null) {
            event.reply("Unfortunietly you can only hug people that are on that server! <:pandaCry:1279483498008285244>").setEphemeral(true).queue();
            return;
        }

        if (cooldown.isOnCooldown(sender.getId() + "$" + target.getId())) {
            event.reply(
                    "You just hugged " + target.getAsMention() + " just a few seconds ago! Please wait " + cooldown.getCooldownSeconds(sender.getId() + "$" + target.getId()) + " seoncds! <:pandaCry:1279483498008285244>"
            ).setEphemeral(true).queue();
            return;
        }

        Random random = new Random();

        event.replyEmbeds(
                Main.getNewEmbed(event.getGuild())
                        .setImage(picture.get(random.nextInt(0, picture.size())))
                        .setDescription("**" + sender.getAsMention() + "** has just hugged **" + target.getAsMention() + "**!\n" +
                                "-# " + comment.get(random.nextInt(0, comment.size()))
                                .replace("%sender%", sender.getEffectiveName())
                                .replace("%target%", target.getEffectiveName())
                        )
                        .build()
        ).queue();

        cooldown.setCooldown(sender.getId() + "$" + target.getId(), -1);
    }

    List<String> comment = Arrays.asList(
            "Aww! You're so cute together!",
            "Don't suffocate them for *too* long!",
            "They really needed it.",
            "They feel better already!",
            "Thanks, %sender%!",
            "%target%, say thank you!",
            "Mwah!",
            "You're welcome!",
            "Adorable!",
            "So sweet!",
            "Such a caring friend!",
            "Such wholesomeness!",
            "I want a hug too!",
            "Adorbs!",
            "That's so kind!",
            "That's so nice!",
            "%target%, hug back!"
    );

    List<String> picture = Arrays.asList(
            "https://media.tenor.com/images/7d3a251e2d7bf9af9925137c37bc1a9d/tenor.gif",
            "https://pbs.twimg.com/media/EeLw9t8VAAAzxSi.jpg",
            "https://media.tenor.com/images/884f2d71fd9670f78da7287bc1568267/tenor.gif",
            "https://pbs.twimg.com/profile_images/1031548675078512642/Mdoz8w3X.jpg",
            "https://pbs.twimg.com/media/EYT23TRVcAMHybk.jpg",
            "https://sdl-stickershop.line.naver.jp/products/0/0/1/1303392/android/stickers/12265831.png",
            "https://ih1.redbubble.net/image.1841450223.3357/poster,504x498,f8f8f8-pad,600x600,f8f8f8.jpg",
            "https://pbs.twimg.com/media/ENmra-QU4AAIeNV.jpg",
            "https://cdn130.picsart.com/288152949057211.png",
            "https://i.pinimg.com/originals/b7/49/2c/b7492c8996b25e613a2ab58a5d801924.gif",
            "https://www.pngitem.com/pimgs/m/519-5196830_freetoedit-cute-kawaii-cat-couple-love-hug-cuddle.png",
            "https://www.pinclipart.com/picdir/middle/552-5523276_freetoedit-cute-kawaii-cat-couple-love-hug-cuddle.png",
            "https://media.tenor.com/images/e167c5d8a3c018181f77aff84c7ead8d/tenor.gif",
            "https://media.tenor.com/images/64edbf23a5174e751aee50ee3289286e/tenor.gif",
            "https://i.pinimg.com/originals/0c/cd/91/0ccd912ac62159482be3fa6c1024c9a8.gif"
    );

}
