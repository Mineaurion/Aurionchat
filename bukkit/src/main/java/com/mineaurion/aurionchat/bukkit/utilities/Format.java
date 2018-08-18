package com.mineaurion.aurionchat.bukkit.utilities;

import com.mineaurion.aurionchat.bukkit.AurionChat;

import java.util.regex.Pattern;

public class Format {

    private AurionChat plugin;

    protected static Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-F])");
    protected static Pattern chatMagicPattern = Pattern.compile("(?i)&([K])");
    protected static Pattern chatBoldPattern = Pattern.compile("(?i)&([L])");
    protected static Pattern chatStrikethroughPattern = Pattern.compile("(?i)&([M])");
    protected static Pattern chatUnderlinePattern = Pattern.compile("(?i)&([N])");
    protected static Pattern chatItalicPattern = Pattern.compile("(?i)&([O])");
    protected static Pattern chatResetPattern = Pattern.compile("(?i)&([R])");

    public static String FormatStringColor(String string)
    {
        String allFormated = string;
        allFormated = chatColorPattern.matcher(allFormated).replaceAll("§$1");
        allFormated = allFormated.replaceAll("%", "\\%");
        return allFormated;
    }

    public static String FormatString(String string)
    {
        String allFormated = string;
        allFormated = chatMagicPattern.matcher(allFormated).replaceAll("§$1");
        allFormated = chatBoldPattern.matcher(allFormated).replaceAll("§$1");
        allFormated = chatStrikethroughPattern.matcher(allFormated).replaceAll("§$1");
        allFormated = chatUnderlinePattern.matcher(allFormated).replaceAll("§$1");
        allFormated = chatItalicPattern.matcher(allFormated).replaceAll("§$1");
        allFormated = chatResetPattern.matcher(allFormated).replaceAll("§$1");
        allFormated = allFormated.replaceAll("%", "\\%");
        return allFormated;
    }

    public static String FormatPlayerName(String playerPrefix, String playerDisplayName, String playerSuffix)
    {
        playerPrefix = chatColorPattern.matcher(playerPrefix).replaceAll("§$1");
        playerPrefix = chatMagicPattern.matcher(playerPrefix).replaceAll("§$1");
        playerPrefix = chatBoldPattern.matcher(playerPrefix).replaceAll("§$1");
        playerPrefix = chatStrikethroughPattern.matcher(playerPrefix).replaceAll("§$1");
        playerPrefix = chatUnderlinePattern.matcher(playerPrefix).replaceAll("§$1");
        playerPrefix = chatItalicPattern.matcher(playerPrefix).replaceAll("§$1");
        playerPrefix = chatResetPattern.matcher(playerPrefix).replaceAll("§$1");

        playerSuffix = chatColorPattern.matcher(playerSuffix).replaceAll("§$1");
        playerSuffix = chatMagicPattern.matcher(playerSuffix).replaceAll("§$1");
        playerSuffix = chatBoldPattern.matcher(playerSuffix).replaceAll("§$1");
        playerSuffix = chatStrikethroughPattern.matcher(playerSuffix).replaceAll("§$1");
        playerSuffix = chatUnderlinePattern.matcher(playerSuffix).replaceAll("§$1");
        playerSuffix = chatItalicPattern.matcher(playerSuffix).replaceAll("§$1");
        playerSuffix = chatResetPattern.matcher(playerSuffix).replaceAll("§$1");
        return playerPrefix + playerDisplayName.trim() + playerSuffix;
    }

    public static String FormatStringAll(String string)
    {
        String allFormated = string;
        allFormated = chatColorPattern.matcher(allFormated).replaceAll("§$1");
        allFormated = chatMagicPattern.matcher(allFormated).replaceAll("§$1");
        allFormated = chatBoldPattern.matcher(allFormated).replaceAll("§$1");
        allFormated = chatStrikethroughPattern.matcher(allFormated).replaceAll("§$1");
        allFormated = chatUnderlinePattern.matcher(allFormated).replaceAll("§$1");
        allFormated = chatItalicPattern.matcher(allFormated).replaceAll("§$1");
        allFormated = chatResetPattern.matcher(allFormated).replaceAll("§$1");
        allFormated = allFormated.replaceAll("%", "\\%");
        return allFormated;
    }

}
