package com.example.with.fairly.lengthy.packagename;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.AutoConfigureDataR2dbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MSSQLServerContainer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(
    initializers = TransactionalServiceTest.Initializer.class,
    classes = TransactionalServiceTest.ContextConfig.class
)
class TransactionalServiceTest {

  @ClassRule
  public static MSSQLServerContainer mssqlServerContainer = new MSSQLServerContainer<>()
      .acceptLicense()
      .withPassword("ABCdef123!");

  @Autowired
  private TransactionalService transactionalService;

  @BeforeAll
  static void beforeAll() {
    mssqlServerContainer.start();
  }

  @Test
  void testTransactionalMethod() {
    var entity = transactionalService.transactionalMethod().block();
    assertThat(entity).isNotNull();
    assertThat(entity.id()).isNotNull();
  }

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of(
          "spring.flyway.url=" + mssqlServerContainer.getJdbcUrl(),
          "spring.flyway.user=sa",
          "spring.flyway.password=" + mssqlServerContainer.getPassword(),
          "spring.r2dbc.url=r2dbc:mssql://" + mssqlServerContainer.getHost() + ":" + mssqlServerContainer.getFirstMappedPort() + "/",
          "spring.r2dbc.username=sa",
          "spring.r2dbc.password=" + mssqlServerContainer.getPassword()
      ).applyTo(configurableApplicationContext.getEnvironment());
    }
  }

  @AutoConfigureDataR2dbc
  @EnableR2dbcRepositories
  @Import(TransactionalService.class)
  static class ContextConfig {
  }
}
