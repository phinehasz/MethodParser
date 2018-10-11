package com.zhhiyp.incubator.asm.facade;

import com.zhhiyp.incubator.asm.core.CallGraph;
import com.zhhiyp.incubator.asm.core.MethodLinker;
import com.zhhiyp.incubator.asm.core.SourceMethodParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author zhiyp
 * @date 2018/10/9 0009 21:51
 */
public class DataSourceFacade {

	public static void main(String[] args) {
		String projectRoot = "D:\\workspace\\github-workspace\\leetcode_drift\\out";
		buildSource(projectRoot);
		//最终建立被调用的关系
		CallGraph callGraph = MethodLinker.buildBeInvokedRelation(CallGraph.getInstance());
	}

	//传入一个工程路径
	//搜索下面的class
	//读成流,发给解析类
	public static void buildSource(String projectRoot) {
		File root = new File(projectRoot);
		if (!root.exists()) {
			return;
		}

		if (root.isDirectory()) {
			String path = root.getAbsolutePath();
			if (!path.endsWith(".git") || !path.endsWith(".idea")) {
				//递归
				for (String child : root.list()) {
					buildSource(path + File.separator + child);
				}
			}
		} else {
			if (root.getAbsolutePath().endsWith(".class")) {
				try {
					FileInputStream in = new FileInputStream(root);
					SourceMethodParser.parseSourceClass(in);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
