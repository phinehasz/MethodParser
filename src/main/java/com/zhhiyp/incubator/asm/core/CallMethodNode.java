package com.zhhiyp.incubator.asm.core;

import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhiyp
 * @date 2018/10/10 0010 20:01
 */
public class CallMethodNode {

	private final Set<String> parentSet = new HashSet<>();
	private final Set<String> childSet = new HashSet<>();

	private String methodSig;
	private String className;

	private MethodNode methodNode;

	public CallMethodNode(String methodSig, String className) {
		this.methodSig = methodSig;
		this.className = className;
	}

	public Set<String> getParentSet() {
		return parentSet;
	}

	public Set<String> getChildSet() {
		return childSet;
	}

	public String getMethodSig() {
		return methodSig;
	}

	public void setMethodSig(String methodSig) {
		this.methodSig = methodSig;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public MethodNode getMethodNode() {
		return methodNode;
	}

	public void setMethodNode(MethodNode methodNode) {
		this.methodNode = methodNode;
	}
}
