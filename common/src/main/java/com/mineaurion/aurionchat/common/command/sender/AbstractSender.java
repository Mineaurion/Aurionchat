package com.mineaurion.aurionchat.common.command.sender;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;

import java.util.*;
import java.util.stream.Collectors;

public class AbstractSender<T> implements ServerPlayer {

    private final AurionChatPlugin plugin;
    private final SenderFactory<?, T> factory;
    private final T sender;

    private final UUID id;
    private final String prefix;
    private final String suffix;
    private final String name;
    private final String displayName;
    private final boolean isConsole;

    public AbstractSender(AurionChatPlugin plugin, SenderFactory<?, T> factory, T sender){
        this.plugin = plugin;
        this.factory = factory;
        this.sender = sender;
        this.id = factory.getId(this.sender);
        this.name = factory.getName(this.sender);
        this.displayName = factory.getDisplayName(this.sender);
        this.prefix = factory.getPrefix(this.sender);
        this.suffix = factory.getSuffix(this.sender);
        this.isConsole = this.factory.isConsole(this.sender);
    }

    public AurionChatPlugin getPlugin(){
        return this.plugin;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public String getSuffix() {
        return this.suffix;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void sendMessage(Component message) {
        if(isConsole()){
            for(Component line: splitNewlines(message)){
                this.factory.sendMessage(this.sender, line);
            }
        } else {
            this.factory.sendMessage(this.sender, message);
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        return isConsole() || this.factory.hasPermission(this.sender, permission);
    }

    @Override
    public boolean isConsole() {
        return this.isConsole;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if(!(obj instanceof AbstractSender)) return false;
        final AbstractSender<?> that = (AbstractSender<?>) obj;
        return this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    // Small function to split components using join into separate component
    public static Iterable<Component> splitNewlines(Component message) {
        if (message instanceof TextComponent && message.style().isEmpty() && !message.children().isEmpty() && ((TextComponent) message).content().isEmpty()) {
            LinkedList<List<Component>> split = new LinkedList<>();
            split.add(new ArrayList<>());

            for (Component child : message.children()) {
                if (Component.newline().equals(child)) {
                    split.add(new ArrayList<>());
                } else {
                    Iterator<Component> splitChildren = splitNewlines(child).iterator();
                    if (splitChildren.hasNext()) {
                        split.getLast().add(splitChildren.next());
                    }
                    while (splitChildren.hasNext()) {
                        split.add(new ArrayList<>());
                        split.getLast().add(splitChildren.next());
                    }
                }
            }

            return split.stream().map(input -> {
                switch (input.size()) {
                    case 0:
                        return Component.empty();
                    case 1:
                        return input.get(0);
                    default:
                        return Component.join(JoinConfiguration.builder().build(), input);
                }
            }).collect(Collectors.toList());
        }

        return Collections.singleton(message);
    }
}