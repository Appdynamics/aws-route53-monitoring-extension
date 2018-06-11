/*
 *   Copyright 2018 . AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.route53;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.amazonaws.services.cloudwatch.model.ListMetricsResult;
import com.amazonaws.services.cloudwatch.model.Metric;
import com.appdynamics.extensions.aws.config.IncludeMetric;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by bhuvnesh.kumar on 6/8/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({HealthCheckIDPredicate.class})
@PowerMockIgnore({"org.apache.*, javax.xml.*"})

public class Route53MetricsProcessorTest {

    @Mock
    AmazonCloudWatch amazonCloudWatch;

    @Mock
    ListMetricsResult listMetricsResult;


    private Route53MetricsProcessor rdsMetricsProcessor = new Route53MetricsProcessor(new ArrayList<IncludeMetric>(), new ArrayList<String>());

    @Before
    public void init() throws Exception {
        Mockito.when(amazonCloudWatch.listMetrics(Mockito.any(ListMetricsRequest.class))).thenReturn(listMetricsResult);
        Mockito.when(listMetricsResult.getMetrics()).thenReturn(new ArrayList<Metric>());
    }

    @Test
    public void testGetMetrics(){
        Assert.assertNotNull(rdsMetricsProcessor.getMetrics(amazonCloudWatch, null, new LongAdder()));
    }

}
