/*
 *   Copyright 2018 . AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.route53;

import com.appdynamics.extensions.aws.SingleNamespaceCloudwatchMonitor;
import com.appdynamics.extensions.aws.collectors.NamespaceMetricStatisticsCollector;
import com.appdynamics.extensions.aws.config.Configuration;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessor;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

import static com.appdynamics.extensions.aws.Constants.METRIC_PATH_SEPARATOR;
import static com.appdynamics.extensions.aws.route53.Constants.*;


public class Route53Monitor extends SingleNamespaceCloudwatchMonitor<Configuration> {

    private static final Logger LOGGER = Logger.getLogger(Route53Monitor.class);

    private static final String DEFAULT_METRIC_PREFIX = String.format("%s%s%s%s", CUSTOM_METRICS, METRIC_PATH_SEPARATOR, AMAZON_SERVICE, METRIC_PATH_SEPARATOR);

    public Route53Monitor() {
        super(Configuration.class);
        LOGGER.info(String.format("Using AWS Route53 Monitor Version [%s]",
                this.getClass().getPackage().getImplementationTitle()));
    }

    @Override
    public String getDefaultMetricPrefix() {
        return DEFAULT_METRIC_PREFIX;
    }

    @Override
    public String getMonitorName() {
        return MONITOR_NAME;
    }

    @Override
    protected List<Map<String, ?>> getServers() {
        return (List<Map<String, ?>>) getContextConfiguration().getConfigYml().get("accounts");
    }

    @Override
    protected void initialize(Configuration config) {
        super.initialize(config);
    }

    @Override
    protected NamespaceMetricStatisticsCollector getNamespaceMetricsCollector(
            Configuration config) {
        MetricsProcessor metricsProcessor = createMetricsProcessor(config);

        return new NamespaceMetricStatisticsCollector
                .Builder(config.getAccounts(),
                config.getConcurrencyConfig(),
                config.getMetricsConfig(),
                metricsProcessor,
                config.getMetricPrefix(),
                config.getCustomDashboard(),
                config.getControllerInfo())
                .withCredentialsDecryptionConfig(config.getCredentialsDecryptionConfig())
                .withProxyConfig(config.getProxyConfig())
                .build();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    private MetricsProcessor createMetricsProcessor(Configuration config) {
        return new Route53MetricsProcessor(
                config.getMetricsConfig().getIncludeMetrics(),
                config.getDimensions());
    }


}
