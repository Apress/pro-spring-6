package com.apress.prospring6.five.boot.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by iuliana.cosmina on 18/04/2022
 */
@Component("johnMayer")
public class GrammyGuitarist {
	private static Logger LOGGER = LoggerFactory.getLogger(GrammyGuitarist.class);

	public void sing() {
		LOGGER.info("sing: Wild blue, deeper than I ever knew");
	}

	public void sing(Guitar guitar) {
		LOGGER.info("play: " + guitar.play());
	}

	public void talk(){
		LOGGER.info("talk");
	}

	public void rest(){
		LOGGER.info("zzz");
	}
}
