package com.soin.sgrm.configuration;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.EnviromentConfig;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.soin.sgrm")
public class AppConfig extends WebMvcConfigurerAdapter
		implements WebApplicationInitializer, ApplicationListener<ContextClosedEvent>, Filter {

	EnviromentConfig envConfig = new EnviromentConfig();

	@Autowired
	HandlerInterceptor securityInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(securityInterceptor);
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {

	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.setInitParameter("logRoot", "/WEB-INF/logs/");
		String path = servletContext.getContextPath();
		Sentry.init("https://141576d945314ae2ac8aed58d0d89dad@o409449.ingest.sentry.io/5282061");
		if (path.contains("sgrmprod")) {
			Sentry.getStoredClient().setEnvironment("produccion");
		} else {
			if (path.contains("sgrm_qa")) {
				Sentry.getStoredClient().setEnvironment("qa");
			} else {
				if (path.contains("sgrm_desa")) {
					Sentry.getStoredClient().setEnvironment("desarrollo");
				} else {
					Sentry.getStoredClient().setEnvironment("local");
				}
			}
		}
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		registry.viewResolver(viewResolver);
	}

	/**
	 * Configure ResourceHandlers to serve static resources like CSS/ Javascript
	 * etc...
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasename("messages");
		return source;
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver getResolver() throws IOException {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		int size = (Constant.MAXFILEUPLOADSIZE * 1024) * 1024;

		resolver.setMaxUploadSize(size * 2);
		resolver.setMaxUploadSizePerFile(size);
		return resolver;
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername(envConfig.getEntry("mailUser"));
		mailSender.setPassword(envConfig.getEntry("mailPassword"));

		Properties props = mailSender.getJavaMailProperties();
		Map<String, String> emailprop = envConfig.getEntryProperties("mailProperties");
		emailprop.forEach((k, v) -> props.put(k, v));

		return mailSender;
	}

	@Bean
	public HandlerExceptionResolver sentryExceptionResolver() {
		return new io.sentry.spring.SentryExceptionResolver();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
