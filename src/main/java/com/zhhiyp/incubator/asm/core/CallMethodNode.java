package com.zhhiyp.incubator.asm.core;

import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhiyp
 * @date 2018/10/10 0010 20:01
 */
public class CallMethodNode {

	//methodNode之间直接维持父子关系
	private final Set<CallMethodNode> parentMethods = new HashSet<>();
	private final Set<CallMethodNode> childMethods = new HashSet<>();
	//根据方法内操作指令记录调用的方法
	private final Set<String> childSet = new HashSet<>();

	private String methodSig;
	private String className;

	private MethodNode methodNode;
	//考虑到接口,抽象类需要实现类去调用该方法,需要父子关系统计
	private ClassNode classNode;

	public CallMethodNode(String methodSig, String className) {
		this.methodSig = methodSig;
		this.className = className;
	}

	public Set<CallMethodNode> getParentMethods() {
		return parentMethods;
	}

	public Set<CallMethodNode> getChildMethods() {
		return childMethods;
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

	public ClassNode getClassNode() {
		return classNode;
	}

	public void setClassNode(ClassNode classNode) {
		this.classNode = classNode;
	}
}
