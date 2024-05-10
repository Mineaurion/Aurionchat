package com.mineaurion.aurionchat.forge;

import com.mineaurion.aurionchat.common.plugin.AurionChatBootstrap;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkConstants;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

@Mod(value = AurionChatPlugin.MOD_ID)
public class Bootstrap implements AurionChatBootstrap {
    private final ModContainer loader;

    private AurionChat plugin;

    private MinecraftServer server;

    public Bootstrap(){
        this.loader = ModList.get().getModContainerByObject(this).orElse(null);
        markAsNotRequiredClientSide();

        if(FMLEnvironment.dist.isClient()){
            System.out.println("Skipping " + AurionChatPlugin.NAME + " init (not supported on the client");
            return;
        }
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);

        this.plugin = new AurionChat(this);
    }

    public void onCommonSetup(FMLCommonSetupEvent event){
        MinecraftForge.EVENT_BUS.register(this);
        this.plugin.registerEarlyListeners();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerAboutToStart(ServerStartingEvent event){
        this.server = event.getServer();
        this.plugin.enable();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onServerStopping(ServerStoppingEvent event){
        this.plugin.disable();
        MinecraftForge.EVENT_BUS.unregister(this);
        this.server = null;
    }

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get().resolve(AurionChatPlugin.MOD_ID).toAbsolutePath();
    }

    public void registerListeners(Object target){
        MinecraftForge.EVENT_BUS.register(target);
    }


    private static void markAsNotRequiredClientSide() {
        try {
            ModLoadingContext.class.getDeclaredMethod("registerExtensionPoint", Class.class, Supplier.class)
                    .invoke(
                            ModLoadingContext.get(),
                            IExtensionPoint.DisplayTest.class,
                            (Supplier<?>) () -> new IExtensionPoint.DisplayTest(
                                    () -> NetworkConstants.IGNORESERVERONLY,
                                    (a, b) -> true
                            )
                    );
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    public Optional<MinecraftServer> getServer(){
        return Optional.ofNullable(this.server);
    }
}
