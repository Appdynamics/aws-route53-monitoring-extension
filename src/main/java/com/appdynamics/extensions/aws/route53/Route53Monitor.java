/*
 *   Copyright 2018 . AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.route53;

import static com.appdynamics.extensions.aws.Constants.METRIC_PATH_SEPARATOR;

import com.appdynamics.extensions.aws.SingleNamespaceCloudwatchMonitor;
import com.appdynamics.extensions.aws.collectors.NamespaceMetricStatisticsCollector;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessor;
import com.appdynamics.extensions.aws.route53.config.Route53Configuration;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Route53Monitor extends SingleNamespaceCloudwatchMonitor<Route53Configuration> {

    private static final Logger LOGGER = Logger.getLogger(Route53Monitor.class);

    private static final String DEFAULT_METRIC_PREFIX = String.format("%s%s%s%s",
            "Custom Metrics", METRIC_PATH_SEPARATOR, "Amazon Route53", METRIC_PATH_SEPARATOR);

    public Route53Monitor() {
        super(Route53Configuration.class);
    }

    @Override
    public String getDefaultMetricPrefix() {
        return DEFAULT_METRIC_PREFIX;
    }

    @Override
    public String getMonitorName() {
        return "Route53Monitor";
    }

    @Override
    protected int getTaskCount() {
        return 3;
    }

    @Override
    protected void initialize(Route53Configuration config) {
        super.initialize(config);
    }

    @Override
    protected NamespaceMetricStatisticsCollector getNamespaceMetricsCollector(
            Route53Configuration config) {
        MetricsProcessor metricsProcessor = createMetricsProcessor(config);

        return new NamespaceMetricStatisticsCollector
                .Builder(config.getAccounts(),
                config.getConcurrencyConfig(),
                config.getMetricsConfig(),
                metricsProcessor,
                config.getMetricPrefix())
                .withCredentialsDecryptionConfig(config.getCredentialsDecryptionConfig())
                .withProxyConfig(config.getProxyConfig())
                .build();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    private MetricsProcessor createMetricsProcessor(Route53Configuration config) {
        return new Route53MetricsProcessor(
                config.getMetricsConfig().getIncludeMetrics(), config.getIncludeDBIdentifiers());
    }

    public static void main(String[] args) throws TaskExecutionException, IOException {

        ConsoleAppender ca = new ConsoleAppender();
        ca.setWriter(new OutputStreamWriter(System.out));
        ca.setLayout(new PatternLayout("%-5p [%t]: %m%n"));
        ca.setThreshold(Level.DEBUG);
        LOGGER.getRootLogger().addAppender(ca);


      /*FileAppender fa = new FileAppender(new PatternLayout("%-5p [%t]: %m%n"), "cache.log");
      fa.setThreshold(Level.DEBUG);
      LOGGER.getRootLogger().addAppender(fa);*/


        final Route53Monitor monitor = new Route53Monitor();

        final Map<String, String> taskArgs = new HashMap<String, String>();
        taskArgs.put("config-file", "/Users/venkata.konala/AppDynamics/Repos/Extensions/aws-api-gateway-monitoring-extension/src/main/resources/conf/config.yml");

        //monitor.execute(taskArgs, null);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    monitor.execute(taskArgs, null);
                } catch (Exception e) {
                    LOGGER.error("Error while running the task", e);
                }
            }
        }, 2, 60, TimeUnit.SECONDS);

    }


}
