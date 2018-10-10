package com.zhhiyp.incubator.asm.core;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ListIterator;

/**
 * @author zhiyp
 * @date 2018/10/10 0010 7:54
 * 解析class文件 获得一个方法及其之中的真实调用
 */
public class SourceMethodParser {

	private static CallGraph callGraph = CallGraph.getInstance();

	public static void parseSourceClass(InputStream in) {
		ClassNode cn = new ClassNode();
		try {
			ClassReader cr = new ClassReader(in);
			cr.accept(cn, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//记录该类
		callGraph.putClass(cn.name);

		if (cn.methods != null && cn.methods.size() > 0) {
			for (MethodNode mn : cn.methods) {
				//记录该类的method
				CallMethodNode cMethodNode = new CallMethodNode(mn.name + "#" + mn.desc, cn.name);
				callGraph.putMethod(cn.name, mn.name + "#" + mn.desc, cMethodNode);
				cMethodNode.setMethodNode(mn);

				if (mn.instructions.size() == 0 || "<clinit>".equals(mn.name)) {
					continue;
				}

				ListIterator<AbstractInsnNode> iterator = mn.instructions.iterator();
				while (iterator.hasNext()) {
					AbstractInsnNode insnNode = iterator.next();
					if (insnNode instanceof MethodInsnNode) {
						//储存该方法内 方法调用指令节点
						String methodOwner = ((MethodInsnNode) insnNode).owner;
						String methodName = ((MethodInsnNode) insnNode).name;
						String methodDesc = ((MethodInsnNode) insnNode).desc;
						cMethodNode.getChildSet().add(methodOwner+"#"+methodName+"#"+methodDesc);
					}
				}
			}
		}

	}
}
