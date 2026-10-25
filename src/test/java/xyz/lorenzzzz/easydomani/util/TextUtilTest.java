package xyz.lorenzzzz.easydomani.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

class TextUtilTest {
    private static String plain(Component c) {
        return PlainTextComponentSerializer.plainText().serialize(c);
    }

    @Test
    void convertiCodiciLegacyInTag() {
        assertEquals("<red><bold>x<reset>", TextUtil.convert("&c&lx&r"));
    }

    @Test
    void mescolaLegacyEMiniMessage() {
        Component c = TextUtil.parse("&8[<#3ddc84>ok&8]");
        assertEquals("[ok]", plain(c));
        String mm = MiniMessage.miniMessage().serialize(c);
        assertTrue(mm.equalsIgnoreCase("<dark_gray>[<#3ddc84>ok<dark_gray>]"), mm);
    }

    @Test
    void sostituisceIPlaceholder() {
        assertEquals("ciao Steve 100%", plain(TextUtil.parse("ciao %player% 100%", "player", "Steve")));
    }

    @Test
    void prefissoFidatoVieneParsato() {
        Component c = TextUtil.parseWithPrefix("&8[<red>EDN&8]", "%prefix% %player%", "player", "x");
        assertEquals("[EDN] x", plain(c));
    }

    @Test
    void valoriDelClientNonIniettanoTag() {
        String evil = "<click:run_command:/op x><red>&c&l";
        Component c = TextUtil.parse("brand: %brand%", "brand", evil);
        assertEquals("brand: " + evil, plain(c));
        assertTrue(c.children().stream().allMatch(ch -> ch.clickEvent() == null));
        assertNull(c.clickEvent());
        // nemmeno nel prefisso: %prefix% non riceve valori client
        Component p = TextUtil.parseWithPrefix("&7P", "%prefix% %player%", "player", "<click:run_command:/op x>");
        assertEquals("P <click:run_command:/op x>", plain(p));
    }
}
