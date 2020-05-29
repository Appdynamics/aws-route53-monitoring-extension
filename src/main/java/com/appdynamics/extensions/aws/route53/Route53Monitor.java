/*
 * Copyright (c) 2018 AppDynamics,Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appdynamics.extensions.aws.route53;

import com.appdynamics.extensions.aws.SingleNamespaceCloudwatchMonitor;
import com.appdynamics.extensions.aws.collectors.NamespaceMetricStatisticsCollector;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessor;
import com.appdynamics.extensions.aws.route53.configuration.Route53Configuration;
import com.appdynamics.extensions.aws.route53.processors.Route53MetricsProcessor;
import com.appdynamics.extensions.logging.ExtensionsLoggerFactory;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import java.util.List;
import java.util.Map;


/**
 * Created by venkata.konala on 4/23/18.
 */
public class Route53Monitor extends SingleNamespaceCloudwatchMonitor<Route53Configuration>{

    private static final Logger logger = ExtensionsLoggerFactory.getLogger(Route53Monitor.class);


    public Route53Monitor(){
        super(Route53Configuration.class);
    }

    @Override
    protected NamespaceMetricStatisticsCollector getNamespaceMetricsCollector(Route53Configuration route53Configuration) {

        MetricsProcessor metricsProcessor = createMetricsProcessor(route53Configuration);
        return new NamespaceMetricStatisticsCollector.Builder(route53Configuration.getAccounts(),
                route53Configuration.getConcurrencyConfig(),
                route53Configuration.getMetricsConfig(),
                metricsProcessor,
                route53Configuration.getMetricPrefix())
                .withCredentialsDecryptionConfig(route53Configuration.getCredentialsDecryptionConfig())
                .withProxyConfig(route53Configuration.getProxyConfig())
                .build();
    }

    private MetricsProcessor createMetricsProcessor(Route53Configuration route53Configuration){
        return new Route53MetricsProcessor(route53Configuration);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getDefaultMetricPrefix() {
        return "Custom Metrics|Amazon Route53";
    }

    @Override
    public String getMonitorName() {
        return "AWSRoute53Monitor";
    }

    @Override
    protected List<Map<String, ?>> getServers() {
        return Lists.newArrayList();
    }
}
