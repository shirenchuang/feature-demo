package com.shirc.middleware.features.javaagent;



import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author shirenchuang
 * @date 2019/12/14  12:49 下午
 */

public class AgentMain {

	private static final Logger logger = LoggerFactory.getLogger(AgentMain.class);

	private static final String injectedClassName = "com.daimler.cap.discovery.Discovery";
	private static final String methodName = "writeToZK";
	public static String localVersion  ;

	static {
		localVersion = System.getProperty("localVersion");
	}



	public static void premain(String agentOps, Instrumentation inst) {
		instrument(agentOps, inst);
	}

	public static void agentmain(String agentOps, Instrumentation inst) {
		instrument(agentOps, inst);
	}

	private static void instrument(String agentOps, Instrumentation inst) {

		inst.addTransformer(new ClassFileTransformer() {
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
				return transformClass(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
			}
		});
	}

	private static byte[] transformClass(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
		className = className.replace("/",".");

		if(className.equals(injectedClassName)){
			logger.info("=======加载类{}======",injectedClassName);

			if(localVersion == null||localVersion.length()==0){
				logger.warn("==== 当前不是迭代版本~~ 不增强~~！=====");
				return null;
			}
			logger.warn("====当前是迭代版本~~ 对 Discovery 做修改！====");

			//javassist
			CtClass ctclass = null;

			try {
				//获得指定类字节码
				ClassPool classPool = ClassPool.getDefault();
				classPool.appendClassPath(new LoaderClassPath(Thread.currentThread()
				.getContextClassLoader()));

				ctclass = classPool.get(className);


				//获取方法实例
				CtMethod ctMethod = ctclass.getDeclaredMethod(methodName);
				ctMethod.insertBefore("System.out.println(\"javaassist 改字节码啦！！！！！\");");
				ctMethod.insertBefore("String localVersion = System.getProperty(\"localVersion\");\n" +
						"    System.out.println(\"=======本地变量:\"+localVersion);\n" +
						"    apiInfo.setAppName(apiInfo.getAppName()+\"_dev_\"+localVersion);");
				return ctclass.toBytecode();
			} catch (NotFoundException | CannotCompileException | IOException e) {
				e.printStackTrace();
			}

		}
		return null;
	}


}

