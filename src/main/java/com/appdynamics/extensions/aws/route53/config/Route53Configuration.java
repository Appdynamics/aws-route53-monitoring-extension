/*
 *   Copyright 2018 . AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.route53.config;
import com.appdynamics.extensions.aws.config.Configuration;

import java.util.List;

/**
 * Created by bhuvnesh.kumar on 6/6/18.
 */
public class Route53Configuration extends Configuration {

    private List<String> includeHealthCheckID;

    public List<String> getIncludeDBIdentifiers() {
        return includeHealthCheckID;
    }

    public void setIncludeDBIdentifiers(List<String> includeDBIdentifiers) {
        this.includeHealthCheckID = includeDBIdentifiers;
    }


}
