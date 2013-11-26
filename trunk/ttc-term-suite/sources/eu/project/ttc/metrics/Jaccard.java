/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package eu.project.ttc.metrics;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Jaccard implements SimilarityDistance {
	
	@Override
	public double getValue(Map<String, Double> source, Map<String, Double> target) {
		double infSum = 0;
		double supSum = 0;
		Set<String> keys = new HashSet<String>();
		keys.addAll(source.keySet());
		keys.addAll(target.keySet());
		for (String key : keys) {
			double sourceValue = source.get(key) == null ? 0 : source.get(key).doubleValue();
			double targetValue = target.get(key) == null ? 0 : target.get(key).doubleValue();
			if (sourceValue < targetValue) {
				infSum += sourceValue;
				supSum += targetValue;
			} else {
				infSum += targetValue;
				supSum += sourceValue;
			}
		}
		if (supSum == 0) {
			return 0;
		} else {
			return infSum / supSum;
		}
	}

}
