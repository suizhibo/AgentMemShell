package xxxx;

import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0)
            return;
        String agentPath = args[0];
//        System.out.print(agentPath);
//        String agentPath = "D:\\Users\\Sangfor\\Desktop\\专项工具开发\\内存码注入\\环境\\javaAgent\\target";
        try {
//            File toolsJar = new File(System.getProperty("java.home").replaceFirst("jre", "lib") + File.separator + "tools.jar");
//            System.out.print(toolsJar.toURI());
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
//            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
//            add.setAccessible(true);
//            add.invoke(classLoader, new Object[]{toolsJar.toURI().toURL()});
            Class<?> MyVirtualMachine = classLoader.loadClass("com.sun.tools.attach.VirtualMachine");
            Class<?> MyVirtualMachineDescriptor = classLoader.loadClass("com.sun.tools.attach.VirtualMachineDescriptor");
            Method list = MyVirtualMachine.getDeclaredMethod("list", new Class[0]);
            List<Object> invoke = (List<Object>) list.invoke((Object) null, new Object[0]);
            for (int i = 0; i < invoke.size(); i++) {
                Object o = invoke.get(i);
                Method displayName = o.getClass().getSuperclass().getDeclaredMethod("displayName", new Class[0]);
                Object name = displayName.invoke(o, new Object[0]);
                System.out.println(String.format("find jvm process name:[[[%s]]]", new Object[]{name.toString()}));
               if (name.toString().contains("org.apache.catalina.startup.Bootstrap")) {
////                if (name.toString().contains("com.example.springbootdemo.SpringBootDemoApplication")) {
//                if (name.toString().contains("com.tongweb.web.thor.startup.ThorBootstrap")) { // tongweb
////                if (name.toString().contains("com.sun.enterprise.glassfish.bootstrap.ASMain")) { // glassfish
////                if (name.toString().contains("weblogic.Server")) {// weblogic
////                if (name.toString().contains("org.eclipse.jetty.xml.XmlConfiguration")) {// jetty
////                if (name.toString().contains("org.jboss.Main")) {// jboss
////                if (name.toString().contains("com.caucho.server.resin.Resin")) {// resin
                    Method attach = MyVirtualMachine.getDeclaredMethod("attach", new Class[]{MyVirtualMachineDescriptor});
                    Object machine = attach.invoke(MyVirtualMachine, new Object[]{o});
                    Method loadAgent = machine.getClass().getSuperclass().getSuperclass().getDeclaredMethod("loadAgent", new Class[]{String.class, String.class});
                    loadAgent.invoke(machine, new Object[]{agentPath, ",pAS3"});
                    Method detach = MyVirtualMachine.getDeclaredMethod("detach", new Class[0]);
                    detach.invoke(machine, new Object[0]);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
