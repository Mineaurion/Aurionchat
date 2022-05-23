package com.mineaurion.aurionchat.sponge;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;

import java.util.Map;
import java.util.UUID;

public class Utils {

    private static final Config config = AurionChat.config;
    private static final Map<UUID, AurionChatPlayer> aurionChatPlayers = AurionChat.aurionChatPlayers;
    private static final LuckPermsUtils luckPermsUtils = AurionChat.luckPermsUtils;

    public static String processMessage(String channel, Text message, Player player){
        String channelFormat = config.channels.get(channel).format;
        String messageColors;
        if(player.hasPermission("aurionchat.chat.colors")){
            messageColors = TextSerializers.FORMATTING_CODE.serialize(message);
        }
        else{
            messageColors = TextSerializers.formattingCode('&').stripCodes(TextSerializers.FORMATTING_CODE.serialize(message));
        }
        return channelFormat
                .replace("{prefix}", getPlayerPrefix(player.getUniqueId()))
                .replace("{suffix}", getPlayerSuffix(player.getUniqueId()))
                .replace("{display_name}", getDisplayName(player))
                .replace("{message}", messageColors);
    }

    public static String getPlayerPrefix(UUID uuid){
        return luckPermsUtils.getPlayerPrefix(uuid);
    }

    public static String getPlayerSuffix(UUID uuid){
        return luckPermsUtils.getPlayerSuffix(uuid);
    }

    public static void sendMessageToPlayer(String channelName, String message){
        Sponge.getServer().getOnlinePlayers().forEach(player -> {
            AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUniqueId());
            if(aurionChatPlayer.getChannels().contains(channelName)){
                player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                if(message.contains("@" + getDisplayName(player) ) || message.contains("@" + player)){
                    player.playSound(config.options.sound, player.getLocation().getPosition(), 10.0);
                    player.sendTitle(Title.builder().subtitle(TextSerializers.FORMATTING_CODE.deserialize("Mention")).fadeIn(20).fadeOut(20).stay(40).build());
                }
            }
        });
    }

    private static String getDisplayName(Player player){
        return TextSerializers.FORMATTING_CODE.serialize(player.getDisplayNameData().displayName().get());
    }

    public static void broadcastToPlayer(String channelName, String message){
        if(Sponge.getServer().getOnlinePlayers().size() > 0){
            for(Player player: Sponge.getServer().getOnlinePlayers()){
                if(player.hasPermission("aurionchat.automessage." + channelName)){
                    player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                }
            }
        }
    }

    public static void sendConsoleMessage(String message){
        Sponge.getGame().getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
    }

}