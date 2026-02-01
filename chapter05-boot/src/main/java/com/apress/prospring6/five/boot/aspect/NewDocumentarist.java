package com.apress.prospring6.five.boot.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by iuliana.cosmina on 18/04/2022
 */
@Component("documentarist")
public class NewDocumentarist {

	protected GrammyGuitarist guitarist;

	public void execute() {
		guitarist.sing();
		Guitar guitar = new Guitar();
		guitar.setBrand("Gibson");
		guitarist.sing(guitar);
		guitarist.talk();
	}

	@Autowired
	@Qualifier("johnMayer")
	public void setGuitarist(GrammyGuitarist guitarist) {
		this.guitarist = guitarist;
	}

	public GrammyGuitarist getGuitarist() {
		return guitarist;
	}
}
