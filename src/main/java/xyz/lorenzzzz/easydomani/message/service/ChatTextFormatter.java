package xyz.lorenzzzz.easydomani.message.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class ChatTextFormatter {
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final Pattern LEGACY_CODE = Pattern.compile("&([0-9a-fk-orA-FK-OR])");
    private static final Pattern PLACEHOLDER = Pattern.compile("%([a-z0-9_-]+)%");
    private static final LegacyComponentSerializer SECTION = LegacyComponentSerializer.builder()
            .character('§').hexColors().useUnusualXRepeatedCharacterHexFormat().build();
    private static final Map<Character, String> TAGS = Map.ofEntries(
            Map.entry('0', "black"), Map.entry('1', "dark_blue"), Map.entry('2', "dark_green"), Map.entry('3', "dark_aqua"),
            Map.entry('4', "dark_red"), Map.entry('5', "dark_purple"), Map.entry('6', "gold"), Map.entry('7', "gray"),
            Map.entry('8', "dark_gray"), Map.entry('9', "blue"), Map.entry('a', "green"), Map.entry('b', "aqua"),
            Map.entry('c', "red"), Map.entry('d', "light_purple"), Map.entry('e', "yellow"), Map.entry('f', "white"),
            Map.entry('l', "bold"), Map.entry('o', "italic"), Map.entry('n', "underlined"), Map.entry('m', "strikethrough"),
            Map.entry('k', "obfuscated"), Map.entry('r', "reset"));

    private ChatTextFormatter() {
    }

    static String convert(String s) {
        return LEGACY_CODE.matcher(s).replaceAll(m -> "<" + TAGS.get(Character.toLowerCase(m.group(1).charAt(0))) + ">");
    }

    public static Component parse(String template, String... kv) {
        return parseWithPrefix("", template, kv);
    }

    public static Component parseWithPrefix(String prefix, String template, String... kv) {
        TagResolver.Builder resolvers = TagResolver.builder().resolver(Placeholder.parsed("prefix", convert(prefix)));
        List<String> keys = new ArrayList<>(List.of("prefix"));
        for (int i = 0; i + 1 < kv.length; i += 2) {
            keys.add(kv[i]);
            resolvers.resolver(Placeholder.unparsed(kv[i], kv[i + 1] == null ? "" : kv[i + 1]));
        }

        String body = PLACEHOLDER.matcher(convert(template)).replaceAll(m ->
                keys.contains(m.group(1)) ? Matcher.quoteReplacement("<" + m.group(1) + ">") : Matcher.quoteReplacement(m.group()));
        return MM.deserialize(body, resolvers.build());
    }

    public static String legacy(Component c) {
        return SECTION.serialize(c);
    }
}
