package com.mineaurion.aurionchat.sponge;

import com.mineaurion.aurionchat.common.UtilsCommon;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Utils extends UtilsCommon<AurionChatPlayer> {

    public Utils(){
        super(AurionChat.aurionChatPlayers);
    }

    public static void sendConsoleMessage(String message){
        Sponge.getGame().getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
    }

}