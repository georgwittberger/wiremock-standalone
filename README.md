# WireMock Standalone

> Project template to operate [WireMock](http://wiremock.org/) as a standalone server

## Overview

[WireMock](http://wiremock.org/) is a popular mock server used for stubbing APIs. It is typically used as part of the testing process of an application to simulate the behavior of external systems. There are several options to run the mock server, e.g. inside of JUnit tests, using a Java API and also as a standalone Java application. Especially the standalone variant is interesting for creating mock servers running in the cloud. For example, the WireMock server could be hosted on the [Heroku](https://www.heroku.com/) platform.

The goal of this project is to facilitate the installation of WireMock on cloud platforms by providing solutions for the following challenges:

- Securing the admin API endpoints. This project provides an extension to enable HTTP basic authentication for the WireMock admin API, so that access can be restricted to authorized users. The stub endpoints are not affected and can still be available to everyone.
- Building the standalone WireMock application and the stub configuration into a Docker container. This project provides a ready-made `Dockerfile` to help you put everything together and run your mock server as a Docker container.
- Running the WireMock server on Heroku. This project provides the foundation for running the mock server on the Heroku platform, see instructions below.

## Local Setup

The following steps allow you to operate WireMock on your local machine.

1. Prepare the required software on your machine.

   - Java Development Kit (version 8 or higher), command `java` available in your `PATH` environment
   - Maven (version 3.5 or higher), command `mvn` available in your `PATH` environment

2. Clone this Git repository.
3. Open a terminal in the project directory.
4. Build and download the required JAR files.

   ```bash
   ./build-libs.sh
   ```

5. Put your stub configurations in the `mappings` directory and referenced body files in the `__files` directory.
6. Optionally put a users configuration file named `admin-users.conf` in the project root to restrict access for the admin API. The file should contain one `username:password` entry per line.
7. Optionally set the HTTP port of the server in the environment variable `PORT` (default is 8080).

   ```bash
   export PORT=3000
   ```

8. Optionally set additional WireMock command line options in the environment variable `WIREMOCK_OPTS` (see the [documentation](http://wiremock.org/docs/running-standalone/)). Do not set `--port` or `--extensions` here!

   ```bash
   export WIREMOCK_OPTS="--print-all-network-traffic"
   ```

9. Optionally set additional extension classes to be loaded in the environment variable `WIREMOCK_EXTENSIONS` (see the [documentation](http://wiremock.org/docs/extending-wiremock/)).

   ```bash
   export WIREMOCK_EXTENSIONS="com.mycompany.SomeCoolRequestFilter"
   ```

10. Run the WireMock server.

```bash
./run-wiremock.sh
```

_Tipp for Windows users: You can run the bash scripts using the Git bash. But you have to adjust the classpath in the run script to separate the JAR files with semicolons (;) instead of colons._

You can test the connection to the admin API using the following cURL command (adjust user credentials and port accordingly).

```bash
curl -v -X GET -u admin:admin "http://localhost:8080/__admin/requests"
```

## Docker Setup

The following steps allow you to build a Docker container for your mock server.

1. Prepare the [Docker](https://docs.docker.com/get-started/) engine on your machine.
2. Clone this Git repository.
3. Put your stub configurations in the `mappings` directory and referenced body files in the `__files` directory.
4. Optionally put a users configuration file named `admin-users.conf` in the project root to restrict access for the admin API. The file should contain one `username:password` entry per line.
5. Open a terminal in the project directory.
6. Build the Docker image from the project (mind the dot at the end of the command line).

   ```bash
   docker build -t mycompany/wiremock:1.0 .
   ```

7. Run the Docker container.

   ```bash
   docker run -p 8080:8080 mycompany/wiremock:1.0
   ```

You can expose the mock server on a different host port using `-p 3000:8080` instead.

You can pass additional WireMock command line options by adding `-e WIREMOCK_OPTS=...` to the run command.

## Heroku Setup

The following steps allow you to run WireMock on the Heroku platform.

1. Prepare [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli) on your machine.
2. Clone this Git repository.
3. Put your stub configurations in the `mappings` directory and referenced body files in the `__files` directory.
4. Optionally put a users configuration file named `admin-users.conf` in the project root to restrict access for the admin API. The file should contain one `username:password` entry per line.
5. Open a terminal in the project directory.
6. Log in to your Heroku account.

   ```bash
   heroku login
   ```

7. Create a new Heroku app or attach an existing app.

   - New app:

     ```bash
     heroku create
     ```

   - Existing app (adjust to match your app name):

     ```bash
     heroku git:remote -a thawing-inlet-61413
     ```

8. Configure the container stack for the app.

   ```bash
   heroku stack:set container
   ```

9. Push the project to Heroku.

   ```bash
   git push heroku master
   ```

You can set the config var `WIREMOCK_OPTS` in the app settings to pass additional WireMock command line options.

See also the following articles for Heroku deployment.

- <https://devcenter.heroku.com/articles/git>
- <https://devcenter.heroku.com/articles/build-docker-images-heroku-yml>

## License

[MIT](https://opensource.org/licenses/MIT)
