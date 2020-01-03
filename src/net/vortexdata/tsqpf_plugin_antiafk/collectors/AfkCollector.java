package net.vortexdata.tsqpf_plugin_antiafk.collectors;

import com.github.theholywaffle.teamspeak3.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.*;
import javafx.util.converter.*;
import net.vortexdata.tsqpf.plugins.*;

import java.util.*;

public class AfkCollector implements Runnable {

    private final PluginConfig config;
    private final PluginLogger logger;
    private final TS3Api api;
    private int sleep;
    private int afkChannelId;
    private int maxIdleTime;
    private boolean usePrivateChannelClause;
    private boolean useWhitelistedGroupsClause;
    private ArrayList<Integer> whitelistedGroups;

    public AfkCollector(PluginConfig config, PluginLogger logger, TS3Api api) {
        this.config = config;
        this.logger = logger;
        this.api = api;

        try {
            this.usePrivateChannelClause = Boolean.parseBoolean(config.readValue("usePrivateChannelClause"));
        } catch (Exception e) {
            this.usePrivateChannelClause = true;
            logger.printError("Failed to parse config value for key 'usePrivateChannelClause', therefor falling back to default value of true. Please check you config and reload the plugin.");
        }

        try {
            this.useWhitelistedGroupsClause = Boolean.parseBoolean(config.readValue("useGroupWhitelistClause"));

            if (useWhitelistedGroupsClause) {

                whitelistedGroups = new ArrayList<>();

                try {
                    Arrays.stream(config.readValue("whitelistedGroups").split(",")).forEach(x -> {
                        whitelistedGroups.add(Integer.parseInt(x));
                    });

                    if (whitelistedGroups.size() < 1) {
                        logger.printWarn("No values in 'whitelistedGroups', setting clause 'useGroupWhitelistClause' to false.");
                        useWhitelistedGroupsClause = false;
                    }

                } catch (Exception e) {
                    logger.printError("Failed to parse config value for key 'whitelistedGroups', disabling clause for 'useGroupWhitelistClause'.");
                    useWhitelistedGroupsClause = false;
                }


            }

        } catch (Exception e) {
            this.useWhitelistedGroupsClause = false;
            logger.printError("Failed to parse config value for key 'useGroupWhitelistClause', therefor falling back to default value of false. Please check you config and reload the plugin.");
        }


        try {
            this.maxIdleTime = Integer.parseInt(config.readValue("maxIdleTimeInSeconds")) * 1000;
        } catch (NumberFormatException e) {
            this.maxIdleTime = 900 * 1000;
            logger.printError("Failed to parse config value for key 'maxIdleTimeInSeconds', therefor falling back to default value of 900. Please check you config and reload the plugin.");
        }

        try {
            this.afkChannelId = Integer.parseInt(config.readValue("afkChannelId"));
        } catch (NumberFormatException e) {
            this.afkChannelId = 1;
            logger.printError("Failed to parse config value for key 'afkChannelId', therefor falling back to default value of 1. Please check you config and reload the plugin.");
        }

        try {
            this.sleep = Integer.parseInt(config.readValue("afkClientCollectionIntervalInSeconds")) * 1000;
        } catch (NumberFormatException e) {
            logger.printError("Failed to parse config value for key 'afkClientCollectionIntervalInSeconds', therefor falling back to default value of 10. Please check you config and reload the plugin.");
            this.sleep = 10000;
        }
    }

    @Override
    public void run() {

        try {

            while (true) {

                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    logger.printWarn("Encountered an interrupted exception while sleeping.");
                }

                List<Client> clients = api.getClients();
                for (Client client : clients) {
                    moveClient(client);
                }

            }

        } catch (Exception e) {
            logger.printDebug("AFK collector service encountered an exception, stopping...");
        }

    }

    public void moveClient(Client client) {
        try {
            if (client.getId() != api.whoAmI().getId() && client.getChannelId() != afkChannelId && client.getIdleTime() > maxIdleTime) {

                // Test if client is whitelisted and return if that is the case
                if (useWhitelistedGroupsClause) {
                    for (int c : client.getServerGroups()) {
                        if (whitelistedGroups.contains(c)) {
                            return;
                        }
                    }
                }

                try {
                    if (usePrivateChannelClause) {
                        if (!api.getChannelInfo(client.getChannelId()).getName().contains(config.readValue("privateChannelStaticString"))) {
                            api.moveClient(client.getId(), afkChannelId);
                            api.sendPrivateMessage(client.getId(), config.readValue("messageClientMoved"));
                        }
                    }


                    else {
                        api.moveClient(client.getId(), afkChannelId);
                        api.sendPrivateMessage(client.getId(), config.readValue("messageClientMoved"));
                    }
                } catch (Exception e) {
                    logger.printWarn("Failed to move client " + client.getNickname() + " to AFK channel, dumping error info: " + e.getMessage());
                }

            }
        } catch (Exception e) {
            logger.printWarn("Failed to fetch client details, dumping error info: " + e.getMessage());
        }
    }

}
