package com.zhhiyp.incubator.asm.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhiyp
 * @date 2018/10/10 0010 19:59
 * 储存方法调用关系
 */
public class CallGraph {
	/**
	 * className <methodSignature,node>
	 * methodSignature = methodName + # + desc
	 */
	private Map<String, Map<String,CallMethodNode>> invokeMap = new HashMap<>();

	private static CallGraph callGraph = new CallGraph();

	private CallGraph(){}

	public static CallGraph getInstance(){
		return callGraph;
	}

	public  Map<String, Map<String, CallMethodNode>> getInvokeMap() {
		return invokeMap;
	}

	public void putClass(String className){
		invokeMap.put(className,new HashMap<>());
	}

	public void putMethod(String className,String methodSig,CallMethodNode methodNode){
		if(!invokeMap.containsKey(className)){
			putClass(className);
		}
		invokeMap.get(className).put(methodSig,methodNode);
	}
}
