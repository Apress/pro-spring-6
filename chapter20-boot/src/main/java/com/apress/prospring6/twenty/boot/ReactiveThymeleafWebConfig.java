/*
Freeware License, some rights reserved

Copyright (c) 2020 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospring6.twenty.boot;

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.thymeleaf.spring6.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring6.view.reactive.ThymeleafReactiveViewResolver;

/**
 * Created by Iuliana Cosmina on 02/07/2020
 */
@Configuration
@EnableConfigurationProperties(ThymeleafProperties.class)
public class ReactiveThymeleafWebConfig implements WebFluxConfigurer {

	private final ISpringWebFluxTemplateEngine thymeleafTemplateEngine;

	public ReactiveThymeleafWebConfig(ISpringWebFluxTemplateEngine templateEngine) {
		this.thymeleafTemplateEngine = templateEngine;
	}

		@Bean
		public ThymeleafReactiveViewResolver thymeleafReactiveViewResolver() {
			var viewResolver = new ThymeleafReactiveViewResolver();
			viewResolver.setTemplateEngine(thymeleafTemplateEngine);
			viewResolver.setOrder(1);
			return viewResolver;
		}

		@Override
		public void configureViewResolvers(ViewResolverRegistry registry) {
			registry.viewResolver(thymeleafReactiveViewResolver());
		}

		@Bean
		public MessageSource messageSource() {
			var messageSource = new ResourceBundleMessageSource();
			messageSource.setBasenames("i18n/global");
			messageSource.setDefaultEncoding("UTF-8");
			return messageSource;
		}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatterForFieldAnnotation(new DateTimeFormatAnnotationFormatterFactory());
	}

}
