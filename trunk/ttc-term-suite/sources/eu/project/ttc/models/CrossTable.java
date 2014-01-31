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
package eu.project.ttc.models;

import java.util.HashMap;
import java.util.Map;

public class CrossTable {

    private Map<String, Integer> scores;
    private Map<String, Integer> coScores;
    private int total;

    private void addScores(Double value, String term, Map<String, Integer> scores) {
        Integer score = scores.get(term);
        if (score == null) {
            score = new Integer(value.intValue());
        } else {
            score = new Integer(score.intValue() + value.intValue());
        }
        scores.put(term, score);

    }

    private void set() {
        this.scores = new HashMap<String, Integer>();
        this.coScores = new HashMap<String, Integer>();
        this.total = 0;
        for (String term : this.index.keySet()) {
            Context context = this.index.get(term);
            for (String coTerm : context.getCoocurringTerms()) {
                double score = context.getOccurrences(coTerm);
                this.total += score;
                this.addScores(score, term, this.scores);
                this.addScores(score, coTerm, this.coScores);
            }
        }
    }

    private Map<String, Context> index;

    public void setContexts(Map<String, Context> index) {
        this.index = index;
        this.set();
    }

    private String term;

    public void setTerm(String term) {
        this.term = term;
    }

    private String coTerm;

    public void setCoTerm(String coTerm) {
        this.coTerm = coTerm;
    }

    private int a;

    private void setA() {
        Double score = this.index.get(this.term).getOccurrences(this.coTerm);
        if (score == null) {
            throw new NullPointerException("term " + this.term + "[" + this.coTerm + "]");
        } else {
            this.a = score.intValue();
        }
        // FIXME
        /*
		Double coScore = this.index.getTermContexts().get(this.coTerm).getCoOccurrences().get(this.term);
		if (coScore == null) {
			throw new NullPointerException(this.index.getLanguage() + ": co-term " + this.coTerm + "[" + this.term + "]");
		}
		if (score.equals(coScore)) {
			this.a = score.intValue();
		} else {
			throw new Exception("Wrong value: " + this.term + "[" + this.coTerm + "] == " + score + " != " + coScore + " == " + this.coTerm + "[" + this.term + "]");
		}
		*/
    }

    public int getA() {
        return this.a;
    }

    private int b;

    private void setB() {
        Integer occ = this.scores.get(this.term);
        if (occ == null) {
            throw new NullPointerException("index of term: " + this.term);
        } else {
            this.b = occ.intValue() - this.a;
        }
		/*
		TermContext context = this.index.getTermContexts().get(this.term);
		if (context == null) {
			throw new NullPointerException("No co-term context for computing the cross-table of " + this.term + " " + this.coTerm);
		} else {
			for (String key : context.getCoOccurrences().keySet()) ;{
				Double coOcc = context.getCoOccurrences().get(key);
				this.b += coOcc.doubleValue();
			}
			this.b -= this.a;
		}
		*/
    }

    public int getB() {
        return this.b;
    }

    private int c;

    private void setC() {
        Integer occ = this.coScores.get(this.coTerm);
        if (occ == null) {
            // throw new NullPointerException("index of co-term: " + this.coTerm);
            this.c = 0;
        } else {
            this.c = occ.intValue() - this.a;
        }
		/*
		this.c = 0;
		/* TODO
		for (String term : this.index.getTermContexts().keySet()) {
			TermContext context = this.index.getTermContexts().get(term);
			Double coOcc = context.getCoOccurrences().get(this.coTerm);
			if (coOcc != null) {
				this.c += coOcc.intValue();
			}
		}
		/*
		TermContext context = this.index.getTermContexts().get(this.coTerm);
		if (context == null) {
			throw new NullPointerException("No co-term context for computing the cross-table of " + this.term + " " + this.coTerm);
		} else {
			for (String key : context.getCoOccurrences().keySet()) {
				Double coOcc = context.getCoOccurrences().get(key);
				this.c += coOcc.doubleValue();
			}
			this.c -= this.a;
		}
		*/
    }

    public int getC() {
        return this.c;
    }

    private int d;

    private void setD() {
        this.d = this.total;
        this.d -= this.a;
        this.d -= this.b;
        this.d -= this.c;
    }

    public int getD() {
        return this.d;
    }

    public void compute() {
        this.setA();
        this.setB();
        this.setC();
        this.setD();
    }

}
