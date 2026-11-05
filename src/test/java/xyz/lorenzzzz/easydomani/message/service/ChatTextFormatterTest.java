package xyz.lorenzzzz.easydomani.message.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

class ChatTextFormatterTest {
    private static String plain(Component c) {
        return PlainTextComponentSerializer.plainText().serialize(c);
    }

    @Test
    void convertiCodiciLegacyInTag() {
        assertEquals("<red><bold>x<reset>", ChatTextFormatter.convert("&c&lx&r"));
    }

    @Test
    void mescolaLegacyEMiniMessage() {
        Component c = ChatTextFormatter.parse("&8[<#3ddc84>ok&8]");
        assertEquals("[ok]", plain(c));
        String mm = MiniMessage.miniMessage().serialize(c);
        assertTrue(mm.equalsIgnoreCase("<dark_gray>[<#3ddc84>ok<dark_gray>]"), mm);
    }

    @Test
    void sostituisceIPlaceholder() {
        assertEquals("ciao Steve 100%", plain(ChatTextFormatter.parse("ciao %player% 100%", "player", "Steve")));
    }

    @Test
    void prefissoFidatoVieneParsato() {
        Component c = ChatTextFormatter.parseWithPrefix("&8[<red>EDN&8]", "%prefix% %player%", "player", "x");
        assertEquals("[EDN] x", plain(c));
    }

    @Test
    void valoriDelClientNonIniettanoTag() {
        String evil = "<click:run_command:/op x><red>&c&l";
        Component c = ChatTextFormatter.parse("brand: %brand%", "brand", evil);
        assertEquals("brand: " + evil, plain(c));
        for (var ch : c.children()) assertTrue(ch.clickEvent() == null);
        assertNull(c.clickEvent());

        Component p = ChatTextFormatter.parseWithPrefix("&7P", "%prefix% %player%", "player", "<click:run_command:/op x>");
        assertEquals("P <click:run_command:/op x>", plain(p));
    }
}
