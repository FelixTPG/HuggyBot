package eu.felixtpg.events;

import net.dv8tion.jda.api.events.thread.GenericThreadEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ThreadCreateEvent extends ListenerAdapter {

    @Override
    public void onGenericThread(GenericThreadEvent event) {
        event.getThread().join().queue();
    }

}
