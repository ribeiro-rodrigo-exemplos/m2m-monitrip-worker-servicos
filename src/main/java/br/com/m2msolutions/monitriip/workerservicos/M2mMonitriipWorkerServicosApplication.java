package br.com.m2msolutions.monitriip.workerservicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class M2mMonitriipWorkerServicosApplication {

	public static void main(String[] args) {

	    SpringApplication.run(M2mMonitriipWorkerServicosApplication.class, args);
	}

	@PostConstruct
	public void configuraTimeZone(){
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
