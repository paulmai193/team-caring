package com.ttth.teamcaring.config;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.shield.ShieldPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(CloudElasticSearchCondition.class)
public class CloudElasticSearchCongfiguration {

    /** The Constant DEFAULT_PORT. */
    private static final int DEFAULT_PORT = 9300;
    /** The log. */
    private final Logger     log          = LoggerFactory
            .getLogger(CloudElasticSearchCongfiguration.class);

    /** The client. */
    private TransportClient  client;
    /** The host name. */
    private String           hostName;
    /** The port number. */
    private int              portNumber;
    /** The cluster name. */
    @Value("${spring.data.elasticsearch.cluster-name:}")
    private String           clusterName;
    /** The authentication. */
    @Value("${spring.data.elasticsearch.properties.shield.user:}")
    private String           authentication;
    /** The ssl enabled. */
    @Value("${spring.data.elasticsearch.properties.shield.transport.ssl:false}")
    private boolean          sslEnabled;
    /** The ip 6 enabled. */
    @Value("${spring.data.elasticsearch.properties.ip6:false}")
    private boolean          ip6Enabled;
    /** The ip 4 enabled. */
    @Value("${spring.data.elasticsearch.properties.ip4:true}")
    private boolean          ip4Enabled;
    /** The compress. */
    @Value("${spring.data.elasticsearch.properties.action.bulk.compress:false}")
    private boolean          compress;
    /** The ping schedule. */
    @Value("${spring.data.elasticsearch.properties.transport.ping_schedule:5s}")
    private String           pingSchedule;

    /** The sniff. */
    @Value("${spring.data.elasticsearch.properties.client.transport.sniff:false}")
    private boolean          sniff;

    /** The pingTimeout. */
    @Value("${spring.data.elasticsearch.properties.client.transport.ping_timeout:5s}")
    private String           pingTimeout;

    /** The nodesSamplerInterval. */
    @Value("${spring.data.elasticsearch.properties.client.transport.nodes_sampler_interval:5s}")
    private String           nodesSamplerInterval;

    /** The dns refresh enabled. */
    @Value("${spring.data.elasticsearch.properties.custom.dns_refresh_enabled:false}")
    private boolean          dnsRefreshEnabled;

    /** The dns refresh time. */
    @Value("${spring.data.elasticsearch.properties.custom.dns_refresh_time:60000}")
    private long             dnsRefreshTime;

    /** The dns refresher timer. */
    private Timer            dnsRefresherTimer;

    /**
     * Sets the host name.
     *
     * @param hostName the hostName to set
     */
    @Value("${spring.data.elasticsearch.cluster-nodes:}")
    public void setHostName(String hostName) {
        int index = hostName.indexOf(":");
        if (index >= 0) {
            this.hostName = hostName.substring(0, index);
            this.portNumber = Integer.parseInt(hostName.substring(index + 1));
        }
        else {
            this.hostName = hostName;
            this.portNumber = DEFAULT_PORT;
        }
        this.clusterName = hostName.split("\\.", 2)[0];
    }

    /**
     * Client.
     *
     * @return the client
     */
    @Bean
    public Client client() {
        log.info("Initializing ES client for cloud: {}:{}", this.hostName, this.portNumber);
        Settings settings = Settings.settingsBuilder().put("transport.ping_schedule", pingSchedule)
                .put("client.transport.sniff", sniff)
                .put("client.transport.ping_timeout", pingTimeout)
                .put("client.transport.nodes_sampler_interval", nodesSamplerInterval)
                .put("cluster.name", clusterName).put("action.bulk.compress", compress)
                .put("shield.transport.ssl", sslEnabled)
                .put("request.headers.X-Found-Cluster", clusterName)
                .put("shield.user", authentication).build();
        client = TransportClient.builder().addPlugin(ShieldPlugin.class).settings(settings).build();
        resolveTransportAddress();
        startDnsRefresherTask();
        return client;
    }

    private void resolveTransportAddress() {
        log.debug("ES DNS Resolve Transport Address: {}", this.hostName);
        try {
            for (InetAddress address : InetAddress.getAllByName(hostName)) {
                if ((ip6Enabled && address instanceof Inet6Address)
                        || (ip4Enabled && address instanceof Inet4Address)) {
                    log.debug("ES DNS Resolve Transport Address result: {}", address);
                    client.addTransportAddress(new InetSocketTransportAddress(address, portNumber));
                }
            }
        }
        catch (UnknownHostException exc) {
            log.error("Unable to find host {}: {}", hostName, exc.getMessage());
        }
    }

    /**
     * The Class DnsRefresherTask.
     */
    class DnsRefresherTask extends TimerTask {

        @Override
        public void run() {
            if (dnsRefreshEnabled) {
                resolveTransportAddress();
            }
        }
    }

    private void startDnsRefresherTask() {
        dnsRefresherTimer = new Timer("ES-DNSRefresherTimer");
        dnsRefresherTimer.scheduleAtFixedRate(new DnsRefresherTask(), dnsRefreshTime,
                dnsRefreshTime);
        log.info("Initializing ES DNS refresh timer task with interval: {} ms - {}", dnsRefreshTime,
                dnsRefreshEnabled);
    }
}
