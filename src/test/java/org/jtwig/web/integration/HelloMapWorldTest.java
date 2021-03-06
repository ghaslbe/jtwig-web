package org.jtwig.web.integration;

import com.google.common.collect.ImmutableMap;
import org.apache.http.client.fluent.Request;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jtwig.web.servlet.JtwigRenderer;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HelloMapWorldTest extends AbstractIntegrationTest {


    @Test
    public void helloWorldTest() throws Exception {
        String content = Request.Get(serverUrl())
                .execute().returnContent().asString();

        assertThat(content, is("Hello Jtwig two!"));
    }

    @Override
    protected void setUpContext(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HelloServlet()), "/*");
    }


    public static class HelloServlet extends HttpServlet {
        private final JtwigRenderer renderer = JtwigRenderer.defaultRenderer();

        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.setAttribute("one", " two");

            renderer.dispatcherFor("/WEB-INF/templates/example.twig")
                    .with(ImmutableMap.<String, Object>builder()
                            .put("name", "Jtwig")
                            .build())
                    .render(request, response);
        }
    }
}
