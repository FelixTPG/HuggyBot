package eu.felixtpg.api.elements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Activity;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class StatusObject {

    @Getter private Activity.ActivityType type;
    @Getter private String name;

}
