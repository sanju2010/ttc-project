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

import java.util.Map;

public class Cosine implements SimilarityDistance {
	
	@Override
	public double getValue(Map<String, Double> source, Map<String, Double> target) {
		double fstSum = 0.0;
		double sndSum = 0.0;
		double sum = 0.0;
		for (String key : source.keySet()) {
			double sourceValue = source.get(key) == null ? 0.0 : source.get(key).doubleValue();
			fstSum += sourceValue * sourceValue;
		}
		for (String key : target.keySet()) {
			double targetValue = target.get(key) == null ? 0.0 : target.get(key).doubleValue();
			sndSum += targetValue * targetValue;
		}
		for (String key : source.keySet()) {
			double sourceValue = source.get(key) == null ? 0.0 : source.get(key).doubleValue();
			double targetValue = target.get(key) == null ? 0.0 : target.get(key).doubleValue();
			sum += sourceValue * targetValue;
		}
		for (String key : target.keySet()) {
			double sourceValue = source.get(key) == null ? 0.0 : source.get(key).doubleValue();
			double targetValue = target.get(key) == null ? 0.0 : target.get(key).doubleValue();
			sum += sourceValue * targetValue;
		}
		// double prod = Math.sqrt(fstSum) * Math.sqrt(sndSum);
		double prod = Math.sqrt(fstSum * sndSum);
		if (prod == 0.0) {
			return 0.0;
		} else {
			return sum / prod;
		}
	}
	
}
