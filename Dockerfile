# Etapa 1: Construção do JAR
FROM gradle:8.10.2-jdk21 AS builder

WORKDIR /app

# Copia os arquivos necessários para o Gradle baixar dependências
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

# Baixa as dependências (sem compilar o projeto ainda)
RUN ./gradlew dependencies --no-daemon || return 0

# Copia o restante do código-fonte
COPY src src

# Compila o projeto e gera o .jar
RUN ./gradlew bootJar --no-daemon

# Etapa 2: Imagem final (leve)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o JAR gerado na etapa anterior
COPY --from=builder /app/build/libs/*.jar app.jar

# Define a porta padrão
EXPOSE 8080

# Comando para executar o app
ENTRYPOINT ["java", "-jar", "app.jar"]
