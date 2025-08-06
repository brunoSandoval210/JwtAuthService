#Contenedor base
FROM eclipse-temurin:21.0.8_9-jdk

#Informar el puerto donde se ejecutara el contenedor
EXPOSE 8080

#Definir directorio raiz del contenedor
WORKDIR /root

#Copiar y pegar archivos dentro del contenedor
COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

#Descargar las dependencias
RUN ./mvnw dependency:go-offline

#COPIAR CODIGO FUENTE
COPY ./src /root/src

#Construir el proyecto
RUN ./mvnw clean install -DskipTests

#Levantar la aplicacion cuando el contenedor inicie
ENTRYPOINT ["java","-jar","/root/target/JwtAuthService-0.0.1-SNAPSHOT.jar"]
