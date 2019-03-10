package com.mineaurion.aurionchat.sponge;

import com.mineaurion.aurionchat.common.AurionChatPlayers;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class Utils {

    private AurionChat plugin;
    private Config config;
    private AurionChatPlayers aurionChatPlayers;

    public Utils(AurionChat plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.aurionChatPlayers = plugin.getAurionChatPlayers();
    }

    public String processMessage(String channel, Text message, Player player){
        String channelFormat = config.channels.get(channel).format;
        String messageColors;
        if(player.hasPermission("aurionchat.chat.colors")){
            messageColors = TextSerializers.FORMATTING_CODE.serialize(message);
        }
        else{
            messageColors = TextSerializers.formattingCode('&').stripCodes(TextSerializers.FORMATTING_CODE.serialize(message));
        }
        return channelFormat
                .replace("{prefix}", getPrefixLuckPerms(player).orElse(""))
                .replace("{display_name}", getDisplayName(player))
                .replace("{message}", messageColors);
    }

    public void sendMessageToPlayer(String channelName, String message){
        Set<UUID> playersListenChannel = aurionChatPlayers.getPlayersListeningChannel(channelName);
        for(UUID uuid: playersListenChannel){
            Optional<Player> player = Sponge.getServer().getPlayer(uuid);
            if(player.isPresent()){
                Player p = player.get();
                p.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                if(message.contains("@" + getDisplayName(p) ) || message.contains("@" + player)){
                    p.playSound(config.options.sound, p.getLocation().getPosition(), 10.0);
                    p.sendTitle(Title.builder().subtitle(TextSerializers.FORMATTING_CODE.deserialize("Mention")).fadeIn(20).fadeOut(20).stay(40).build());
                }
            }

        }
    }

    private Optional<String> getPrefixLuckPerms(Player player){
        Optional<LuckPermsApi> api = AurionChat.luckPermsApi;
        Optional<String> prefix = Optional.empty();
        if(api.isPresent()){
            Optional<User> user = api.get().getUserSafe(player.getUniqueId());
            if(user.isPresent()){
                Contexts contexts = api.get().getContextsForPlayer(player);
                UserData userData = user.get().getCachedData();
                MetaData metaData = userData.getMetaData(contexts);
                prefix = Optional.ofNullable(metaData.getPrefix());
            }
        }
        return prefix;
    }

    private Optional<String> getSuffixLuckPerms(Player player){
        Optional<LuckPermsApi> api = AurionChat.luckPermsApi;
        Optional<String> prefix = Optional.empty();
        if(api.isPresent()){
            Optional<User> user = api.get().getUserSafe(player.getUniqueId());
            if(user.isPresent()){
                Contexts contexts = api.get().getContextsForPlayer(player);
                UserData userData = user.get().getCachedData();
                MetaData metaData = userData.getMetaData(contexts);
                prefix = Optional.ofNullable(metaData.getSuffix());
            }
        }
        return prefix;
    }

    private String getDisplayName(Player player){
        return TextSerializers.FORMATTING_CODE.serialize(player.getDisplayNameData().displayName().get());
    }

    public void broadcastToPlayer(String channelName, String message){
        String channelPermission = config.automessage.get(channelName).permission;
        if(Sponge.getServer().getOnlinePlayers().size() > 0){
            for(Player player: Sponge.getServer().getOnlinePlayers()){
                if(player.hasPermission(channelPermission)){
                    player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                }
            }
        }
    }

}