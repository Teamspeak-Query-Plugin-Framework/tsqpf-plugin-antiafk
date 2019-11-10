package net.vortexdata.tsqpf_plugin_antiafk;

import net.vortexdata.tsqpf.plugins.*;
import net.vortexdata.tsqpf_plugin_antiafk.collectors.*;

public class Main extends TeamspeakPlugin {

    @Override
    public void onEnable() {
        getConfig().setDefault("maxIdleTimeInSeconds", "900");
        getConfig().setDefault("afkClientCollectionIntervalInSeconds", "10");
        getConfig().setDefault("afkChannelId", "1");
        getConfig().setDefault("messageClientMoved", "I've moved you to the AFK channel to make room for other users, as you idled for more than 15 minutes.");
        getConfig().setDefault("usePrivateChannelClause", "true");
        getConfig().setDefault("privateChannelStaticString", "Private Channel");
        getConfig().saveAll();

        new Thread(new AfkCollector(getConfig(), getLogger(), getAPI())).start();
    }

    @Override
    public void onDisable() {

    }

}
