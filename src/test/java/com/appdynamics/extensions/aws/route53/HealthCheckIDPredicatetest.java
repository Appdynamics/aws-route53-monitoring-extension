/*
 *   Copyright 2018 . AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.route53;

import com.appdynamics.extensions.yml.YmlReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by bhuvnesh.kumar on 6/8/18.
 */
public class HealthCheckIDPredicatetest {

    List<String> identifier = (List<String>)(YmlReader.readFromFile(new File("src/test/resources/conf/itest-encrypted-config.yaml")).get("includeHealthCheckID"));

    private HealthCheckIDPredicate classUnderTest = new HealthCheckIDPredicate(identifier);

    @Test
    public void testPatternPredicate(){
        Assert.assertTrue(classUnderTest.getPatternPredicate().apply(identifier.get(0)));
    }


}
