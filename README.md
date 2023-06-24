[![Java CI with Gradle](https://github.com/Nilstrieb/very-totally-secure-perfect-app-that-is-not-vulnerable/actions/workflows/gradle.yml/badge.svg)](https://github.com/Nilstrieb/very-totally-secure-perfect-app-that-is-not-vulnerable/actions/workflows/gradle.yml)

# Vulnerapp

-- A Vulnerable Sample Spring Boot Application

This application uses a relatively modern stack but is still vulernable to a set of attacks.
Featuring:

- [XSS](https://portswigger.net/web-security/cross-site-scripting)
- [SQLi](https://portswigger.net/web-security/sql-injection)
- [CSRF](https://portswigger.net/web-security/csrf)
- [SSRF](https://portswigger.net/web-security/ssrf)
- Fake Logins
- Info Exposure
- Plain Passwords
- ...

```console
./gradlew bootRun
```

# Improvements

## Security

Many security improvements have been added to the vulnerapp.

### JWTs

Instead of basic auth, the vulnerapp now uses JWTs. This enhances security because it makes CSRF impossible
(CSRF is still enabled as an extra line of defense for the future, see the comment in the config) without the
need for CSRF tokens. It also ensures that the authentication flow is fully within control of the application,
turning the login into an actual login and allowing logout. Allowing logout improves security if the browser
is shared between people (since the token is persisted in the sessionStorage, ending the session does the logout
automatically).

The JWT key is checked into the repo and inside `resources`. In a production environment, it would make more sense
to mount it in a docker volume and supply the keys via environment variables, but this is not implemented.

### SSRF /health

The `/health` endpoint used to have a SSRF vulnerability, as it blindly trusted the `Host` header in the request.
This was not very smart, because the user could just set the host to their own server and leak the admin password.

This has been changed so that the request always goes to `localhost` (hardcoded) with the correct port extracted
from the configuration.

### Deletion of whoami

`/whoami` used to have a SQL injection vulnerability. This was fixed by doing the query properly, but the endpoint
is not needed anymore because of the JWTs. Therefore, it can be deleted, as every deleted endpoint reduces the
attack surface of the application.

### Input validation

The vulnerapp now does input validation, limiting the size of posts and checking that the user doesn't use
obviously insecure passwords.

### RBAC

Role based access control is used to limit specific actions. For example, only admins can edit users and only posters
can create posts.

### XSS

The frontend uses DOM API manipulation apis and `textContent` to create the posts, making them immune to XSS.

### Password hashing

The password is hashed using a delegating password encoding, bcrypt by default. This makes sure that the plaintext
password cannot be stolen.

## Other Improvements

There are also two UI changes in the frontend. Error messages from validation/login are now shown as bootstrap
alerts and the creation dates are rendered in a nicer format.

## Development

There are also a few improvements that don't necessarily increase the security of the vulnerapp but make it nicer
to work with it.

### Github Actions CI

There is a Github Actions CI pipeline that runs `./gradlew test`. This ensures that the application passes all of
the tests. The test report is also uploaded as
a [Github Actions Artifact](https://docs.github.com/en/actions/using-workflows/storing-workflow-data-as-artifacts)
for further inspection if desired. There's also a small badge in the README.

### nix devshell

To allow NixOS users (me!) to work on the vulnerapp. No nix build has been set up because gradle is
an absolute nix nightmare. Maven should be less painful, but I haven't bothered to switch.