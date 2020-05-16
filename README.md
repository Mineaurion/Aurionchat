# AurionChat

### What is it ?


AurionChat is a cross chat plugin beetween server for bukkit and sponge. We can describe as a chat channel plugin too. This plugin work with rabbitmq to transfer the message beetween the server if you don't know what i'm talking please refer to [this](https://www.rabbitmq.com/) 

### How it works ?

When you send the message if other people listen to the same channel as you typed, they will receive the message. The same apply for you :)

### Features


- Chat channel
- Formating chat
- Bukkit and Sponge support
- Automessage WIP

### Commands


- `channel` : List the channel listened
- `channel <channelName> join` : Listen and Join the desired channel
- `channel <channelName> leave` : Leave the desired channel
- `channel <channelName> spy` : Listen the desired channel
- `channel alllisten` : Listen to all available channel

### Permissions

- `aurionchat.chat.speak`: Can speak in channel
- `aurionchat.chat.colors` : Usage of colors in chat
- `aurionchat.joinchannel.<channelName>` : Auto join the channel
- `aurionchat.listenchannel.<channelName>` : Listen to the channel


---

## Chat

You need a rabbitmq server for the plugin to make it work. Just replace the uri with you user and pass and the host.
You can have more info about the uri here : https://www.rabbitmq.com/uri-spec.html

This an example to declare a channel :
```
channels:
    #You can put any alphanumeric name you want.
    <name>:
        format : "[GLOBAL] {prefix}{display_name} : &f{message}"
        alias: "g" # It's just an alias for the command
```

At this time you can use only this token :

```
{prefix} : Get the prefix from Vault (Bukkit) Or LuckPerms (Sponge)
{suffix} : Get the suffix from Vault (Bukkit) or LuckPerms (Sponge)
{display_name} : Get the displayname you have on the server
{message} : The message :)

You can put any color code beetwen the token or any characters if you want.
```


## Automessage

For this part to work you need to install this [plugin](https://github.com/Mineaurion/AurionChat-AutoMessage)  on one of our server. 

You can disable the automessage for one server in the configurtion just switch this :
```
 enable=true
```

To this : 
```
 enable=false
```

Permission of every automessage channel is : `aurionchat.automessage.<channelName>`

### Discord

If you need support regarding our plugin, come on our [discord](https://discord.gg/Zn4ZbP9)
