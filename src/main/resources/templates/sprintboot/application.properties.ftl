# Puerto del servidor
server.port=8081
server.address=0.0.0.0
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/${dbName}
spring.datasource.username=${dbUser}
spring.datasource.password=${dbPassword}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# CORS global (para Flutter)
spring.web.cors.allowed-origins=*
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true

# Jackson: evitar errores por proxies de Hibernate
spring.jackson.serialization.fail-on-empty-beans=false
