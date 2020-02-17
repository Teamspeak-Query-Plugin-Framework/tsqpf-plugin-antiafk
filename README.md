# AntiAFK
A light-weight anti-AFK plugin for the Teamspeak Query Plugin Framework.	A light-weight anti-AFK plugin for the Teamspeak Query Plugin Framework.

## 💡 How does it work?

I searches for clients that have a longer idle time than is allowed in the plugins config file and moves them to a channel which is defined in the plugin config. It also has a feature for implementation in the Private Channel plugin, which allows the exlusion of AFK checks in private channel.

## 🚀 Gettings started

Just download the latest release that's compatible with your TSQPF version and copy it into its plugin directory. After you've done that, either reload or restart your framework instance in order to get it loaded and initiated.

## ⚙️ Configuration

Here's a list of all config keys, value datatypes and a description:

KEY | DATATYPE | DESCRIPTION

- **maxIdleTimeInSeconds** : [Integer] How long a client can remain AFK in a channel before being moved.
- **privateChannelStaticString** : [String] A string which is present in all private channel names.
- **afkClientCollectionIntervalInSeconds** : [Integer] Interval of how often the afkClientCollector will check for AFK clients.
- **afkChannelId** : [Integer] Channel id of the AFK channel clients get moved to.
- **usePrivateChannelClause** : [Boolean] Tells the plugin if it should exclude private channel from AFK check.
- **messageClientMoved** : [String] Tells the AFK client why he has been moved to the AFK channel.


## 📁 Directory Tree

AntiAFK/<br>
└── plugin.conf<br>

## 📜 Vortexdata Certification

This plugin is developed by VortexdataNET for the Teamspeak Query Plugin Framework. Every release is being tested for any bugs, its performance or security issues. You are free to use, modify or redistribute the plugin.
