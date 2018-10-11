package com.zhhiyp.incubator.asm.core;

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
								//如果是子类对象调用了父类方法,则在子类里注册一个该方法
								//该子类的'新'方法有向上调用,无向下调用
								//该方法的向下调用,其实是去看该父类的对应方法
								//如果父类非本工程类,则该方法链路只能是找向上调用者.
								beInvokedNode = new CallMethodNode(invokeMethodSig,ownerName);
								invokeMap.get(ownerName).put(invokeMethodSig,beInvokedNode);
							}
						}
						//父添加到子的parentMethods
						beInvokedNode.getParentMethods().add(methodParentNode);
						//子添加到父的childMethods
						methodParentNode.getChildMethods().add(beInvokedNode);
					}else {
						//不是本工程类
						//TODO 虚拟方法节点,例如数据流想知道工程哪里调用过List集合的add,所以虚拟该节点,但其childMethods是空.只有parent


					}
				});
			});
		});
		return callGraph;
	}
}
