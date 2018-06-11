/*
 *   Copyright 2018 . AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.route53;

import com.amazonaws.services.cloudwatch.model.Metric;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import java.util.List;

/**
 * Created by bhuvnesh.kumar on 6/8/18.
 */
public class HealthCheckIDPredicate implements Predicate<Metric> {

    private List<String> includeHealthCheckID;
    private Predicate<CharSequence> patternPredicate;

    public HealthCheckIDPredicate(List<String> includeHealthCheckID) {
        this.includeHealthCheckID = includeHealthCheckID;
        build();
    }

    private void build() {
        if (includeHealthCheckID != null && !includeHealthCheckID.isEmpty()) {
            for (String pattern : includeHealthCheckID) {
                Predicate<CharSequence> charSequencePredicate = Predicates.containsPattern(pattern);
                if (patternPredicate == null) {
                    patternPredicate = charSequencePredicate;
                } else {
                    patternPredicate = Predicates.or(patternPredicate, charSequencePredicate);
                }
            }
        }
    }

    public boolean apply(Metric metric) {

        String healthCheckID = metric.getDimensions().get(0).getValue();

        return patternPredicate.apply(healthCheckID);
    }

    public Predicate<CharSequence> getPatternPredicate() {
        return patternPredicate;
    }


}
