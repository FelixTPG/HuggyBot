package eu.felixtpg.api.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String name();
    String emoji();
    boolean guildOnly() default true;

}
