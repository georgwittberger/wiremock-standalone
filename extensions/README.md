# WireMock Standalone Extensions

> WireMock extensions to facilitate standalone operation

## Admin API Basic Authentication

The extension class `io.github.georgwittberger.wiremockstandalone.AdminBasicAuthRequestFilter` provides a simple HTTP basic authentication to secure the admin API endpoints.

The users allowed to access the admin API are configured in a file named `admin-users.conf` which must be located in the current working directory of the WireMock server.

In the users file there is one `username:password` entry per line. Username and password are delimited by a colon.

```text
admin:topsecret
bob:letmein
```

## License

[MIT](https://opensource.org/licenses/MIT)
