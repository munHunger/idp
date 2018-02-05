package se.munhunger.idp.swagger;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.Swagger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


public class Bootstrap extends HttpServlet {
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Swagger swagger = new Swagger();

        new SwaggerContextService().withServletConfig(config).updateSwagger(swagger);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http", "https"});
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("se.munhunger.idp");
        beanConfig.setScan(true);
    }
}
