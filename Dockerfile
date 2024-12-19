# ビルドステージ
FROM gradle:8.10.2-jdk21 AS build

# 作業ディレクトリを設定
WORKDIR /app

# 必要なファイルをコピー
COPY build.gradle settings.gradle /app/
COPY gradle /app/gradle

# 依存関係のダウンロード
RUN gradle build -x test --parallel --no-daemon || return 0

# ソースコードをコピーしてアプリをビルド
COPY src /app/src
RUN gradle build -x test --no-daemon

# 実行環境ステージ
FROM eclipse-temurin:21

# 作業ディレクトリを設定
WORKDIR /app

# ビルド成果物をコピー
COPY --from=build /app/build/libs/*.jar /app/app.jar

# ポートを公開
EXPOSE 8080

# アプリケーションを起動
ENTRYPOINT ["java", "-jar", "/app/app.jar"]