package com.tengen;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HelloWorldSparkFreemarkerStyle {

	public static void main(String[] args) {
		final Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(HelloWorldSparkFreemarkerStyle.class, "/");
		
		Spark.get("/hello", new Route() {
			
			public Object handle(Request arg0, Response arg1) throws Exception {
				StringWriter writer = new StringWriter();
				
				try {
					Template helloTemplate = configuration.getTemplate("hello.ftl");
					
					Map<String, Object> helloMap = new HashMap<String, Object>();
					helloMap.put("answer", "Sapi Ompong");
					
					helloTemplate.process(helloMap, writer);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				catch (TemplateException e) {
					e.printStackTrace();
				}
				
				return writer;
			}
		});
	}

}
