package eu.felixtpg.modules.quotes;

import eu.felixtpg.Main;
import eu.felixtpg.utils.Embeds;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QuoteManager extends ListenerAdapter {

    public QuoteManager() {
        Main.getJda().upsertCommand(
                Commands.message("Create Quote")
                        .setNameLocalization(DiscordLocale.GERMAN, "Zitat erstellen")
        ).queue();
    }

    @Override
    public void onMessageContextInteraction(MessageContextInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("Create Quote")) return;
        event.deferReply().queue();

        Message message = event.getTarget();

        // Check if the message has actual content
        if (message.getContentDisplay().isEmpty()) {
            event.replyEmbeds(Embeds.getEmbed(Embeds.EmbedType.ERROR, "Diese Nachricht enthält keinen Inhalt!")).setEphemeral(true).queue();
            return;
        }

        final String[] quote = {message.getContentDisplay()};
        message.getMentions().getCustomEmojis().forEach(emoji -> quote[0] = quote[0].replace(emoji.getAsMention(), ""));

        // check if the quote is empty after removing the mentions
        if (quote[0].isEmpty()) {
            event.replyEmbeds(Embeds.getEmbed(Embeds.EmbedType.ERROR, "Diese Nachricht enthält keinen Inhalt!")).setEphemeral(true).queue();
            return;
        }

        try {
            event.getHook().editOriginalAttachments(FileUpload.fromData(createQuote(quote[0], message.getAuthor(), false), "quote.png")).queue();
        } catch (Exception exception) {
            event.replyEmbeds(Embeds.getEmbed(Embeds.EmbedType.ERROR, "Zitat konnte nicht erstellt werden!")).setEphemeral(true).queue();
            exception.printStackTrace();
        }
    }

    private byte[] createQuote(String quote, User user, boolean grayscale) throws IOException, FontFormatException {
        String author = "~ " + user.getEffectiveName();
        String username = "@" + user.getName();

        BufferedImage image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        BufferedImage avatar = ImageIO.read(new URL(user.getEffectiveAvatar().getUrl(2048))); // Append query param for max size

        if (grayscale) {
            avatar = convertToGrayscale(avatar);
        }

        Graphics2D g2d = image.createGraphics();
        g2d.setBackground(Color.BLACK);

        int width = image.getWidth();
        int height = image.getHeight();

        // Draw the avatar on the left side but start a bit outside the image
        g2d.drawImage(avatar, -20, 0, height, height, null);

        g2d.setPaint(new Color(0, 0, 0));
        g2d.fillRect((width / 2), 0, width, height);

        // Apply a gradient from the right side to the middle of the image starting with black from the right
        GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 0, 0, 0), width / 2, 0, new Color(0, 0, 0, 255));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width / 2, height);

        // Set a debug outline to see the gradient
//        g2d.setPaint(new Color(255, 0, 0));
//        g2d.drawRect(0, 0, width / 2, height);

        Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/ggsans-Normal.ttf")).deriveFont(48.0F);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        g2d.setFont(customFont);

        g2d.setColor(Color.WHITE);

        // Initial font size
        int fontSize = 100;
        Font font = loadFont("/fonts/ggsans-Normal.ttf", fontSize);
        g2d.setFont(font);

        // Define the area where the quote should be drawn
        int maxWidth = image.getWidth() / 2; // Quote should not cross the middle
        int xStart = (image.getWidth() / 2) + (image.getWidth() / 4); // Take the half and then the middle of the half
        int yStart = 50; // Start a bit down from the top to ensure there's space for the quote

        // Draw the quote
        List<String> lines = new ArrayList<>();
        String[] words = quote.split(" ");
        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {
            // Check if the word is too long, if yes split it
            while (word.length() > 20) {
                lines.add(word.substring(0, 20));
                word = word.substring(20);
            }

            // Check if the current line with the new word is still within the bounds
            if (font.getStringBounds(currentLine + word, g2d.getFontRenderContext()).getWidth() < maxWidth) {
                currentLine.append(word).append(" ");
            } else {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word + " ");
            }
        }

        // draw final line
        lines.add(currentLine.toString());

        int y = yStart;
        for (String line : lines) {
            // get the start coordinate so that the text is centered
            int x = xStart - (int) font.getStringBounds(line, g2d.getFontRenderContext()).getWidth() / 2;
            y += g2d.getFontMetrics().getHeight();
            g2d.drawString(line, x, y);
        }

        // Draw author and username below the quote

        // get the coordinates from the last line and add some space
        y += g2d.getFontMetrics().getHeight() + 20;


        g2d.setColor(Color.WHITE.darker());
        g2d.setFont(font.deriveFont(Font.PLAIN, fontSize * 0.6f)); // Slightly smaller font for author and username
        g2d.drawString(author, xStart, y); // Leave some space between author and username

        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(font.deriveFont(Font.ITALIC, fontSize * 0.3f)); // Slightly smaller font for author and username
        g2d.drawString(username, xStart, y + g2d.getFontMetrics().getHeight());

        g2d.dispose();
        return imageToByteArray(image);
    }

    private Font loadFont(String path, float size) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
            return font;
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] imageToByteArray(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    private BufferedImage convertToGrayscale(BufferedImage avatar) {
        BufferedImage gray = new BufferedImage(avatar.getWidth(), avatar.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = gray.createGraphics();
        g.drawImage(avatar, 0, 0, null);
        g.dispose();
        return gray;
    }

}
