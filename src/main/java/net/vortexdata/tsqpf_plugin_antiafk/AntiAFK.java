package net.vortexdata.tsqpf_plugin_antiafk;

import net.vortexdata.tsqpf.plugins.*;
import net.vortexdata.tsqpf_plugin_antiafk.collectors.AfkCollector;

public class AntiAFK extends TeamspeakPlugin {

    private Thread collectorProzess;

    @Override
    public void onEnable() {
        getConfig().setDefault("maxIdleTimeInSeconds", "900");
        getConfig().setDefault("afkClientCollectionIntervalInSeconds", "60");
        getConfig().setDefault("afkChannelId", "1");
        getConfig().setDefault("messageClientMoved", "I've moved you to the AFK channel to make room for other users, as you idled for more than 15 minutes.");
        getConfig().setDefault("usePrivateChannelClause", "true");
        getConfig().setDefault("privateChannelStaticString", "Private Channel");
        getConfig().setDefault("useGroupWhitelistClause", "false");
        getConfig().setDefault("whitelistedGroups", "1");
        getConfig().saveAll();

        collectorProzess = new Thread(new AfkCollector(getConfig(), getLogger(), getAPI()));
        collectorProzess.start();
    }

    @Override
    public void onDisable() {
        collectorProzess.interrupt();
    }
}