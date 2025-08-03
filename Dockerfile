FROM openjdk:25-oracle

LABEL maintainer = Yasser

COPY . /ChessGame

WORKDIR ChessGame/out/artifacts/ChessGame_jar

ENV DISPLAY :10

ENTRYPOINT ["java", "-jar", "ChessGame.jar"]