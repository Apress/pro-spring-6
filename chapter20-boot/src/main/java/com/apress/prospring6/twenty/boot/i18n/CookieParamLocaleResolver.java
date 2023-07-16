package com.apress.prospring6.twenty.boot.i18n;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

/**
 * Created by Iuliana Cosmina on 22/07/2020
 * Description: an implementation of LocaleContextResolver that stores the desired locale in cookie with a lifespan of five minutes.
 */
public class CookieParamLocaleResolver implements LocaleContextResolver {

	public static final String LOCALE_REQUEST_ATTRIBUTE_NAME = "Bookstore.Cookie.LOCALE";

	private String languageParameterName;

	public CookieParamLocaleResolver(final String languageParameterName) {
		this.languageParameterName = languageParameterName;
	}

	@Override
	public LocaleContext resolveLocaleContext(final ServerWebExchange exchange) {
		Locale defaultLocale = getLocaleFromCookie(exchange);
		List<String> referLang = exchange.getRequest().getQueryParams().get(languageParameterName);
		if (!CollectionUtils.isEmpty(referLang) ) {
			String lang = referLang.get(0);
			defaultLocale = Locale.forLanguageTag(lang);
			setLocaleToCookie(lang, exchange);
		}
		return new SimpleLocaleContext(defaultLocale);
	}

	private void setLocaleToCookie(final String languageValue, final ServerWebExchange exchange) {
		MultiValueMap<String, HttpCookie> cookies =  exchange.getRequest().getCookies();
		HttpCookie langCookie = cookies.getFirst(LOCALE_REQUEST_ATTRIBUTE_NAME);
		if(langCookie == null || !languageValue.equals(langCookie.getValue())) {
			ResponseCookie cookie = ResponseCookie.from(LOCALE_REQUEST_ATTRIBUTE_NAME, languageValue).maxAge(Duration.ofMinutes(5)).build();
			exchange.getResponse().addCookie(cookie);
		}
	}

	private Locale getLocaleFromCookie(final ServerWebExchange exchange){
		MultiValueMap<String, HttpCookie> cookies =  exchange.getRequest().getCookies();
		HttpCookie langCookie = cookies.getFirst(LOCALE_REQUEST_ATTRIBUTE_NAME);
		return langCookie != null ? Locale.forLanguageTag(langCookie.getValue()) : Locale.getDefault();
	}

	@Override
	public void setLocaleContext(final ServerWebExchange exchange, final LocaleContext localeContext) {
		throw new UnsupportedOperationException("Not Supported");
	}

}
