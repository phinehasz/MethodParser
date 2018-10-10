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
 */
public class SourceMethodParser {

	public static void parseSourceClass(InputStream in){
		ClassNode cn = new ClassNode();
		try {
			ClassReader cr = new ClassReader(in);
			cr.accept(cn,0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(MethodNode mn : cn.methods){

			if(mn.instructions.size() == 0 || "<clinit>".equals(mn.name)){
				continue;
			}

			ListIterator<AbstractInsnNode> iterator = mn.instructions.iterator();
			while(iterator.hasNext()){
				AbstractInsnNode insnNode = iterator.next();
				if(insnNode instanceof MethodInsnNode){
					//方法调用节点
				}
			}
		}

	}
}
