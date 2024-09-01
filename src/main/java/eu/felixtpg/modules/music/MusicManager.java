package eu.felixtpg.modules.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;

public class MusicManager extends ListenerAdapter {

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public MusicManager() {
        this.playerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);

        this.musicManagers = new HashMap<>();
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(musicManager.getPlayer()));

        return musicManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (member == null) {
            event.getChannel().sendMessage("Could not retrieve member information.").queue();
            return;
        }

        if (message.startsWith("!play")) {
            String trackUrl = message.substring("!play".length()).trim();
            playTrack(guild, member, trackUrl, event);
        } else if (message.startsWith("!skip")) {
            skipTrack(guild, event);
        } else if (message.startsWith("!pause")) {
            pauseMusic(guild, event);
        } else if (message.startsWith("!resume")) {
            resumeMusic(guild, event);
        } else if (message.startsWith("!volume")) {
            int volume = Integer.parseInt(message.substring("!volume".length()).trim());
            setVolume(guild, volume, event);
        } else if (message.startsWith("!clear")) {
            clearQueue(guild, event);
        } else if (message.startsWith("!leave")) {
            leaveChannel(guild, event);
        }
    }

    private void playTrack(Guild guild, Member member, String trackUrl, MessageReceivedEvent event) {
        VoiceChannel memberChannel = (VoiceChannel) member.getVoiceState().getChannel();

        if (memberChannel == null) {
            event.getChannel().sendMessage("You need to be in a voice channel to play music.").queue();
            return;
        }

        AudioManager audioManager = guild.getAudioManager();
        audioManager.openAudioConnection(memberChannel);
        audioManager.setSelfDeafened(true);

        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.getScheduler().queue(track);
                event.getChannel().sendMessage("Playing track: " + track.getInfo().title).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                playlist.getTracks().forEach(musicManager.getScheduler()::queue);
                event.getChannel().sendMessage("Added playlist: " + playlist.getName()).queue();
            }

            @Override
            public void noMatches() {
                event.getChannel().sendMessage("No matches found!").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                event.getChannel().sendMessage("Failed to load track: " + e.getMessage()).queue();
            }
        });
    }

    private void skipTrack(Guild guild, MessageReceivedEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.getScheduler().nextTrack();
        event.getChannel().sendMessage("Skipped to the next track.").queue();
    }

    private void pauseMusic(Guild guild, MessageReceivedEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.getPlayer().setPaused(true);
        event.getChannel().sendMessage("Paused the music.").queue();
    }

    private void resumeMusic(Guild guild, MessageReceivedEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.getPlayer().setPaused(false);
        event.getChannel().sendMessage("Resumed the music.").queue();
    }

    private void setVolume(Guild guild, int volume, MessageReceivedEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.getPlayer().setVolume(volume);
        event.getChannel().sendMessage("Set the volume to " + volume + ".").queue();
    }

    private void clearQueue(Guild guild, MessageReceivedEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.getScheduler().clearQueue();
        event.getChannel().sendMessage("Cleared the queue.").queue();
    }

    private void leaveChannel(Guild guild, MessageReceivedEvent event) {
        guild.getAudioManager().closeAudioConnection();
        event.getChannel().sendMessage("Disconnected from the voice channel.").queue();
    }

}