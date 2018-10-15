package com.zhhiyp.incubator.asm.core;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
		callGraph.putClasSNode(cn.name,cn);

		if (cn.methods != null && cn.methods.size() > 0) {
			for (MethodNode mn : cn.methods) {

				//不记录类加载方法
				if ("<clinit>".equals(mn.name)) {
					continue;
				}


				//内部类需要解析
				//如果是类加载器先加载全部jar和class,就只需要以内部类的name去loadClass,只是方式不同.取决于如何读取class
				//如果是直接每个class文件遍历读,内部类则已经读取了.
				//parseInnerClass(cn);

				//记录该类的method
				CallMethodNode cMethodNode = new CallMethodNode(mn.name + "#" + mn.desc, cn.name);
				callGraph.putMethod(cn.name, mn.name + "#" + mn.desc, cMethodNode);
				cMethodNode.setMethodNode(mn);
				cMethodNode.setClassNode(cn);

				//抽象方法就不记录更多
				if (mn.instructions.size() > 0) {
					ListIterator<AbstractInsnNode> iterator = mn.instructions.iterator();
					while (iterator.hasNext()) {
						AbstractInsnNode insnNode = iterator.next();
						if (insnNode instanceof MethodInsnNode) {
							//储存该方法内 方法调用指令节点
							String methodOwner = ((MethodInsnNode) insnNode).owner;
							String methodName = ((MethodInsnNode) insnNode).name;
							String methodDesc = ((MethodInsnNode) insnNode).desc;
							cMethodNode.getChildSet().add(methodOwner + "#" + methodName + "#" + methodDesc);
						}
					}
				}
			}
		}

	}

	private static void parseInnerClass(ClassNode cn) {
		List<InnerClassNode> innerClasses = cn.innerClasses;
		for (InnerClassNode innerClass : innerClasses) {
			if(innerClass.name.equals(cn.innerClasses)){
				//添加新方法 parse
			}
		}
	}
}
