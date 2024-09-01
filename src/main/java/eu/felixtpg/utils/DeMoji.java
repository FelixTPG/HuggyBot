package eu.felixtpg.utils;

import lombok.Getter;

@Getter
public enum DeMoji {

    LL("〢"),
    DL(" 〢 "),
    XX(" › "),
    ARROW("<:pfeil:1242420480535957545>"),

    DY("✅"),
    DN("❌"),
    X("<:deny:1243276982784102470>"),
    Y("<:check:1243276980934152202>"),
    CORNER("<:corner:1222516363805986849>"),
    LINE("<:line:1222517018075336816>"),
    INFO("<:information:1230228745869328505>"),
    WARNING("<a:achtung:1160968499863228536>"),
    MINECRAFT_HEART("<a:HeartPlus:1189994200364765194>"),
    SIMPLICA("<:simplicaNew:1251629186045448244>");

    private final String v;

    DeMoji(String value) {
        this.v = value;
    }

}
