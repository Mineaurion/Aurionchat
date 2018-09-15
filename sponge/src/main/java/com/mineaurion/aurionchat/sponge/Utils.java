package com.mineaurion.aurionchat.sponge;

import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializer;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;

import java.util.Optional;
import java.util.Set;

public class Utils {

    private AurionChat plugin;

    public Utils(AurionChat plugin){
        this.plugin = plugin;
    }

    public String processMessage(String channel, Text message, Player player){
        String channelFormat = plugin.getConfig().getFormatChannel(channel);
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
        for(AurionChatPlayer p: AurionChat.onlinePlayers){
            Set<String> listening = p.getListening();
            if(listening.contains(channelName)){
                Player player = p.getPlayer();
                player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                if(message.contains("@" + getDisplayName(player) ) || message.contains("@" + player)){
                    player.playSound(SoundTypes.ENTITY_EXPERIENCE_ORB_PICKUP, player.getLocation().getPosition(), 10.0);
                    player.sendTitle(Title.builder().subtitle(TextSerializers.FORMATTING_CODE.deserialize("Mention")).fadeIn(20).fadeOut(20).stay(40).build());
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
                prefix = Optional.of(metaData.getPrefix());
            }
        }
        return prefix;
    }

    private String getDisplayName(Player player){
        return TextSerializers.FORMATTING_CODE.serialize(player.getDisplayNameData().displayName().get());
    }

}