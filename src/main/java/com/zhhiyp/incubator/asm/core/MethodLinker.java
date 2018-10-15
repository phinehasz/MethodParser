package com.zhhiyp.incubator.asm.core;

import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.util.Map;

/**
 * @author zhiyp
 * @date 2018/10/10 0010 21:30
 */
public class MethodLinker {

	public static CallGraph buildBeInvokedRelation(CallGraph callGraph) {
		Map<String, Map<String, CallMethodNode>> invokeMap = callGraph.getInvokeMap();

		invokeMap.forEach((className, classMethodMap) -> {
			//基于类
			classMethodMap.forEach((methodSig, methodParentNode) -> {
				//基于每个方法node
				methodParentNode.getChildSet().forEach((classMethodSig) -> {
					// 包名类名#方法名#方法描述
					String ownerName = classMethodSig.substring(0, classMethodSig.indexOf('#'));
					String invokeMethodSig = classMethodSig.substring(classMethodSig.indexOf('#') + 1);
					//明确是 调用了本工程的类
					if (invokeMap.containsKey(ownerName)) {
						CallMethodNode beInvokedNode = invokeMap.get(ownerName).get(invokeMethodSig);
						if (beInvokedNode == null) {
							//排除枚举的ordinal方法
							if ("ordinal#()I".equals(invokeMethodSig)) {
								return;
							} else {
								//search in superClass
								ClassNode classNode = callGraph.getClassNode(ownerName);
								if(classNode.superName != null && !"java/lang/Object".equals(classNode.superName)){
									beInvokedNode = invokeMap.get(classNode.superName).get(invokeMethodSig);
									if(beInvokedNode == null){
										//can't happen unless superClass hasn't been recorded
										//virtual supperClass
										virtualAndLinkNode(callGraph,methodParentNode,classNode.superName,invokeMethodSig);
									}else{
										link(methodParentNode, beInvokedNode);
										return;
									}
								}

							}
						}
						link(methodParentNode, beInvokedNode);
					}else {
						//不是本工程类
						//虚拟方法节点,例如数据流想知道工程哪里调用过List集合的add,所以虚拟该节点,但其childMethods是空.只有parent
						virtualAndLinkNode(callGraph, methodParentNode, ownerName, invokeMethodSig);
					}
				});
			});
		});
		return callGraph;
	}

	private static void virtualAndLinkNode(CallGraph callGraph, CallMethodNode methodParentNode, String ownerName, String invokeMethodSig) {
		CallMethodNode virtualMethod = new CallMethodNode(invokeMethodSig,ownerName);
		callGraph.putVirtualMethod(ownerName,invokeMethodSig,virtualMethod);
		link(methodParentNode, virtualMethod);
	}

	private static void link(CallMethodNode methodParentNode, CallMethodNode beInvokedNode) {
		//父添加到子的parentMethods
		beInvokedNode.getParentMethods().add(methodParentNode);
		//子添加到父的childMethods
		methodParentNode.getChildMethods().add(beInvokedNode);
	}
}
