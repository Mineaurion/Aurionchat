package com.mineaurion.aurionchat.sponge;

import com.mineaurion.aurionchat.common.AurionChatPlayers;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class Utils {

    private Config config;
    private AurionChatPlayers aurionChatPlayers;
    private Optional<LuckPermsUtils> luckPermsUtils;

    public Utils(AurionChat plugin){
        this.config = plugin.getConfig();
        this.aurionChatPlayers = plugin.getAurionChatPlayers();
        this.luckPermsUtils = plugin.getLuckPermsUtils();
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
                .replace("{prefix}", getPlayerPrefix(player.getUniqueId()))
                .replace("{suffix}", getPlayerSuffix(player.getUniqueId()))
                .replace("{display_name}", getDisplayName(player))
                .replace("{message}", messageColors);
    }

    public String getPlayerPrefix(UUID uuid){
        return luckPermsUtils.map(permsUtils -> permsUtils.getPlayerPrefix(uuid)).orElse("");
    }

    public String getPlayerSuffix(UUID uuid){
        return luckPermsUtils.map(permsUtils -> permsUtils.getPlayerSuffix(uuid)).orElse("");
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

    private String getDisplayName(Player player){
        return TextSerializers.FORMATTING_CODE.serialize(player.getDisplayNameData().displayName().get());
    }

    public void broadcastToPlayer(String channelName, String message){
        if(Sponge.getServer().getOnlinePlayers().size() > 0){
            for(Player player: Sponge.getServer().getOnlinePlayers()){
                if(player.hasPermission("aurionchat.automessage." + channelName)){
                    player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                }
            }
        }
    }

}