package xxxx;

import java.util.*;

public class Constants {
    static HashMap<Object, Object> params = new HashMap<Object, Object>();
    static{params.put("javax.servlet.http.HttpServlet", Arrays.asList("$1", "$2"));
        params.put("org.apache.catalina.core.ApplicationFilterChain", Arrays.asList("$1", "$2"));
        params.put("com.tongweb.web.thor.core.ApplicationFilterChain", Arrays.asList("$1", "$2"));
        params.put("org.eclipse.jetty.servlet.ServletHandler$Chain", Arrays.asList("$1", "$2"));
        params.put("weblogic.servlet.internal.ServletStubImpl", Arrays.asList("$1", "$2"));
        params.put("com.caucho.server.dispatch.ServletInvocation", Arrays.asList("$1", "$2"));
        params.put("com.caucho.server.dispatch.PageFilterChain", Arrays.asList("$1", "$2"));
        params.put("weblogic.servlet.internal.WebAppServletContext$ServletInvocationAction", Arrays.asList("$2", "$3"));
    }


    public static Object getTarget(){
        Map<String, Map<String, Object>> targetClasses = new HashMap<String, Map<String, Object>>();
//        Map<String, Object> targetClassJavaxMap = new HashMap<String, Object>();
//        targetClassJavaxMap.put("methodName", "service");
//        List<String> paramJavaxClsStrList = new ArrayList<String>();
//        paramJavaxClsStrList.add("javax.servlet.ServletRequest");
//        paramJavaxClsStrList.add("javax.servlet.ServletResponse");
//        targetClassJavaxMap.put("paramList", paramJavaxClsStrList);
//        targetClasses.put("javax.servlet.http.HttpServlet", targetClassJavaxMap);

        Map<String, Object> targetClassMap = new HashMap<String, Object>();
        targetClassMap.put("methodName", "doFilter");
        List<String> paramClsStrList = new ArrayList<String>();
        paramClsStrList.add("javax.servlet.ServletRequest");
        paramClsStrList.add("javax.servlet.ServletResponse");
        targetClassMap.put("paramList", paramClsStrList);
        targetClasses.put("org.apache.catalina.core.ApplicationFilterChain", targetClassMap);

        targetClassMap = new HashMap<String, Object>();
        targetClassMap.put("methodName", "doFilter");
        paramClsStrList = new ArrayList<String>();;
        paramClsStrList.add("javax.servlet.ServletRequest");
        paramClsStrList.add("javax.servlet.ServletResponse");
        targetClassMap.put("paramList", paramClsStrList);
        targetClasses.put("org.eclipse.jetty.servlet.ServletHandler$Chain", targetClassMap);


        targetClassMap = new HashMap<String, Object>();
        targetClassMap.put("methodName", "doFilter");
        paramClsStrList = new ArrayList<String>();
        paramClsStrList.add("javax.servlet.ServletRequest");
        paramClsStrList.add("javax.servlet.ServletResponse");
        targetClassMap.put("paramList", paramClsStrList);
        targetClasses.put("com.tongweb.web.thor.core.ApplicationFilterChain", targetClassMap);

        targetClassMap = new HashMap<String, Object>();
        targetClassMap.put("methodName", "service");
        paramClsStrList = new ArrayList<String>();
        paramClsStrList.add("javax.servlet.ServletRequest");
        paramClsStrList.add("javax.servlet.ServletResponse");
        targetClassMap.put("paramList", paramClsStrList);
        targetClasses.put("com.caucho.server.dispatch.ServletInvocation", targetClassMap);

//        targetClassMap = new HashMap<String, Object>();
//        targetClassMap.put("methodName", "doFilter");
//        paramClsStrList = new ArrayList<String>();
//        paramClsStrList.add("javax.servlet.ServletRequest");
//        paramClsStrList.add("javax.servlet.ServletResponse");
//        targetClassMap.put("paramList", paramClsStrList);
//        targetClasses.put("com.caucho.server.dispatch.PageFilterChain", targetClassMap);

        return targetClasses;
    }

    public static List<String> getParams(String classname){
        return (List<String>) params.get(classname);
    }

}
