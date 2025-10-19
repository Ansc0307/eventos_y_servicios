package com.eventos.ms_reservas;

import com.eventos.ms_reservas.config.TestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(TestContainerConfig.class)
@ActiveProfiles("test")
class MsReservasApplicationTests {

	@Test
	void contextLoads() {
	}

}
