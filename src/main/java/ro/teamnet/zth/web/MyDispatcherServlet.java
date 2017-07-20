package ro.teamnet.zth.web;

import ro.teamnet.zth.api.annotations.MyController;
import ro.teamnet.zth.api.annotations.MyRequestMethod;
import ro.teamnet.zth.appl.controller.DepartmentController;
import ro.teamnet.zth.appl.controller.EmployeeController;
import ro.teamnet.zth.fmk.AnnotationScanUtils;
import ro.teamnet.zth.fmk.MethodAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Cristian.Barbu on 7/20/2017.
 */
public class MyDispatcherServlet extends HttpServlet {

    Map<String,MethodAttributes> allowedMethods;

    public void init(){

        try {
            Iterable<Class> iterClass =    AnnotationScanUtils.getClasses("ro.teamnet.zth.appl.controller");
            for (Class aClass : iterClass) {
                Method[] methods = aClass.getDeclaredMethods();
                for (Method method : methods) {
                    MethodAttributes methodAttribute = new MethodAttributes();
                    methodAttribute.setMethodName(method.getName());
                    methodAttribute.setMethodType(method.getAnnotation(MyRequestMethod.class).methodType());
                    methodAttribute.setControllerClass(aClass.getName());
                    String key="";
                    MyController my = (MyController) aClass.getAnnotation(MyController.class);
                    key = my.urlPath() +
                            method.getAnnotation(MyRequestMethod.class).urlPath() + "/" +
                            method.getAnnotation(MyRequestMethod.class).methodType();

                    allowedMethods.put(key,methodAttribute);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodType = "GET";
        dispatchReply(req, resp, methodType);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodType = "POST";
        dispatchReply(req,resp,methodType);
    }

    public void dispatchReply(HttpServletRequest request, HttpServletResponse response, String methodType ){
        try{
            Object resultToDisplay = dispatch(request, methodType);
            reply(response, resultToDisplay);
        }
        catch (Exception e){
            sendExceptionError(e);
        }
    }

    public Object dispatch(HttpServletRequest request, String methodType  ) {
        String url = request.getPathInfo();
        MethodAttributes attributes = null;
        String path;


        if (!url.startsWith("/departments")
                && !url.startsWith("/employees")
                && !url.startsWith("/jobs")
                && !url.startsWith("/locations"))
            throw new RuntimeException("!!!!");

        path = url + "/" + methodType;

        attributes = allowedMethods.get(path);

        if (attributes != null) {
            try {
                Class returnedClass = Class.forName(attributes.getControllerClass());
                Method returnedMethod = returnedClass.getMethod(attributes.getMethodName());
                return returnedMethod.invoke(returnedClass.newInstance());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    public void reply(HttpServletResponse response, Object result){
        try {
            response.getWriter().write((String) result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendExceptionError(Exception e){
        System.out.println("Error!!!!   " + e.toString());
    }
}
