package ro.teamnet.zth;

import org.codehaus.jackson.map.ObjectMapper;
import ro.teamnet.zth.fmk.MethodAttributes;
import ro.teamnet.zth.fmk.domain.HttpMethod;
import ro.teamnet.zth.utils.BeanDeserializator;
import ro.teamnet.zth.utils.ControllerScanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Z2HDispatcherServlet extends HttpServlet {
    private ControllerScanner controllerScanner;

    @Override
    public void init() throws ServletException {

        controllerScanner = new ControllerScanner("ro.teamnet.zth.appl.controller");
        controllerScanner.scan();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Get ======================");
        dispatchReply(req, resp, HttpMethod.GET);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply(req, resp, HttpMethod.POST);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply(req, resp, HttpMethod.DELETE);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply(req, resp, HttpMethod.PUT);
    }

    private void dispatchReply(HttpServletRequest req, HttpServletResponse resp, HttpMethod methodType) {
        try {
            System.out.println("Get 1 ======================");
            Object resultToDisplay = dispatch(req, methodType);
            System.out.println("Get 2 ======================");
            reply(resp, resultToDisplay);
            System.out.println("Get 3 ======================");
        } catch (Exception e) {
            try {
                System.out.println("Get 3 ======================" + e.getStackTrace());
                sendExceptionError(e, resp);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private void sendExceptionError(Exception e, HttpServletResponse resp) throws IOException {
        resp.getWriter().print(e.getMessage());
    }

    private void reply(HttpServletResponse resp, Object resultToDisplay) {
        //todo serialize the output(resultToDisplay= controllerinstance.invokeMetohd(parameters))
        //todo into JSON using ObjectMapper(Jackson)

        ObjectMapper mapper = new ObjectMapper();

        final String responseAsString;
        try {
            responseAsString = mapper.writeValueAsString(resultToDisplay);
            resp.getWriter().write(responseAsString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object dispatch(HttpServletRequest req, HttpMethod methodType) {

        //todo invoke the controller method for the current request
        String url = req.getPathInfo();
        MethodAttributes methodAttributes = controllerScanner.getMetaData(url, methodType);
        try {
            Object classToInvoke = methodAttributes.getControllerClass().newInstance();
            Method methodToInvoke = methodAttributes.getMethod();
            List<Object> params =  BeanDeserializator.getMethodParams(methodToInvoke,req);

             return methodToInvoke.invoke(classToInvoke, params.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }


}
