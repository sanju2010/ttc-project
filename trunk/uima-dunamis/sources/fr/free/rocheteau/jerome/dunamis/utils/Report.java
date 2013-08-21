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

package fr.free.rocheteau.jerome.dunamis.utils;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.uima.util.ProcessTrace;
import org.apache.uima.util.ProcessTraceEvent;

public class Report {
	
	private DefaultMutableTreeNode root;
	
	private void setRoot() {
		this.root = new DefaultMutableTreeNode("");
	}
	
	private void setRoot(DefaultMutableTreeNode root) {
		this.root = root;
	}
	
	private DefaultMutableTreeNode getRoot() {
		return this.root;
	}
	
	private DefaultTreeModel model;
	
	private void setModel() {
		this.model = new DefaultTreeModel(this.getRoot());
	}
	
	private DefaultTreeModel getModel() {
		return this.model;
	}
	
	private JTree tree;

	private void setTree() {
		this.tree = new JTree(this.getModel());
		this.tree.setRootVisible(false);
	}
  
	private JTree getTree(){
		return this.tree;
	}
	
	private JScrollPane component;
	
	private void setComponent() {
		this.component = new JScrollPane(this.getTree());
		this.component.setPreferredSize(new Dimension(500,300));
		this.component.setBorder(BorderFactory.createTitledBorder("Performance Report"));
	}
	
	public JScrollPane getComponent(){
		return this.component;
	}
	
	public void clear() {
		this.setRoot();
		// this.getComponent().setRootVisible(false);
		this.getModel().setRoot(this.getRoot());
	}
	
	public Report(){
		this.setRoot();
		this.setModel();
		this.setTree();	
		this.setComponent();
	}
	
	public void update(ProcessTrace processTrace) {
		long totalTime = 0;
		Iterator<ProcessTraceEvent> it = processTrace.getEvents().iterator();
		while (it.hasNext()) {
			ProcessTraceEvent event = it.next();
			totalTime += event.getDuration();
		}
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("100% - " + totalTime + "ms - Collection Processing Engine");
		this.setRoot(root);
		this.getTree().setRootVisible(true);
		it = processTrace.getEvents().iterator();
		while (it.hasNext()) {
			ProcessTraceEvent event = (ProcessTraceEvent) it.next();
			this.build(event,root, totalTime);
		}
		this.getModel().setRoot(root);
  }
  
	private void build(ProcessTraceEvent event,DefaultMutableTreeNode root,long totalTime) {
		final DecimalFormat rateFormat = new DecimalFormat("##.##%");
		long duration = event.getDuration();
		double rateValue;
		if (totalTime != 0) {
			rateValue = ((double) duration) / totalTime;
		} else {
			rateValue = 0;
		}
		String rate = rateFormat.format(rateValue);
		String type = event.getType();
		if (type.equals("End of Batch")) {
			/*
			DefaultMutableTreeNode father = (DefaultMutableTreeNode) root.getChildAt(root.getChildCount() - 1);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(rate + " - " + duration + "ms - " + "End of Batch");
			father.add(node);
			*/
		} else {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(rate + " - " + duration + "ms - " + event.getComponentName());
			root.add(node);
			Iterator<ProcessTraceEvent> it = event.getSubEvents().iterator();
			while (it.hasNext()) {
				ProcessTraceEvent ev = it.next();
				this.build(ev,node,totalTime);
			}
		}
	}
  
}
