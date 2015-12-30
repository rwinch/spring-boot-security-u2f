package sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.yubico.u2f.U2F;

@SpringBootApplication
public class U2fApplication {

	@Bean
	public InMemoryRequestStorage requestStorage() {
		return new InMemoryRequestStorage();
	}

	@Bean
	InMemoryDeviceRegistration deviceRegistration() {
		return new InMemoryDeviceRegistration();
	}

	@Bean
	public U2F u2f() {
		return new U2F();
	}

	public static void main(String[] args) {
		SpringApplication.run(U2fApplication.class, args);
	}
}
