# Используем за основу образ OpenJDK 17
FROM openjdk:17

#RUN sudo apt install nodejs -y

#RUN sudo apt install npm -y

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем jar-файл из локальной директории внутрь контейнера
COPY build/libs/RKSP_Coursework-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт, на котором будет работать приложение
# В данном случае, приложение будет доступно на порту 8080
EXPOSE 8080

# Запускаем приложение с помощью команды java -jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
