/*
 *   Copyright 2018 . AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.route53;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.DimensionFilter;
import com.amazonaws.services.cloudwatch.model.Metric;
import com.appdynamics.extensions.aws.config.IncludeMetric;
import com.appdynamics.extensions.aws.dto.AWSMetric;
import com.appdynamics.extensions.aws.metric.NamespaceMetricStatistics;
import com.appdynamics.extensions.aws.metric.StatisticType;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessor;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessorHelper;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author Satish Muddam
 */
public class Route53MetricsProcessor implements MetricsProcessor {

    private static final String NAMESPACE = "AWS/Route53";

    private static final String DIMENSIONS = "HealthCheckId";

    private List<IncludeMetric> includeMetrics;
    private List<String> includeHealthCheckID;

    public Route53MetricsProcessor(List<IncludeMetric> includeMetrics, List<String> includeHealthCheckID) {
        this.includeMetrics = includeMetrics;
        this.includeHealthCheckID = includeHealthCheckID;
    }

    public List<AWSMetric> getMetrics(AmazonCloudWatch awsCloudWatch, String accountName, LongAdder awsRequestsCounter) {

        List<DimensionFilter> dimensions = new ArrayList<DimensionFilter>();

        DimensionFilter dimensionFilter = new DimensionFilter();
        dimensionFilter.withName(DIMENSIONS);

        dimensions.add(dimensionFilter);

        HealthCheckIDPredicate predicate = new HealthCheckIDPredicate(includeHealthCheckID);

        return MetricsProcessorHelper.getFilteredMetrics(awsCloudWatch, awsRequestsCounter, NAMESPACE, includeMetrics, dimensions, (Predicate<Metric>) predicate);

//        return MetricsProcessorHelper.getFilteredMetrics(awsCloudWatch, awsRequestsCounter,
//                NAMESPACE,
//                includeMetrics,
//                dimensions);
    }

    public StatisticType getStatisticType(AWSMetric metric) {
        return MetricsProcessorHelper.getStatisticType(metric.getIncludeMetric(), includeMetrics);
    }


    public List<com.appdynamics.extensions.metrics.Metric> createMetricStatsMapForUpload(NamespaceMetricStatistics namespaceMetricStats) {
        Map<String, String> dimensionToMetricPathNameDictionary = new HashMap<String, String>();
        dimensionToMetricPathNameDictionary.put(DIMENSIONS, "Health Check Id");

        return MetricsProcessorHelper.createMetricStatsMapForUpload(namespaceMetricStats,
                dimensionToMetricPathNameDictionary, false);
    }


    public String getNamespace() {
        return NAMESPACE;
    }

}
