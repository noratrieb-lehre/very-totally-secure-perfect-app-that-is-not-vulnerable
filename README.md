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
This was not very smart, because the user could just set the host to their own server and leak the admin credentials
to their server, trivially taking over the entire application. This was arguably the worst vulnerability because of the
low attack complexity (just spin up a webserver exposed to the internet and log headers), the attack vector being
the network and the resulting privilege being very high (admin).

This has been changed so that the request always goes to `localhost` (hardcoded) with the correct port extracted
from the configuration.

### Deletion of whoami

`/whoami` used to have a SQL injection vulnerability. This was fixed by doing the query properly, but the endpoint
is not needed anymore because of the JWTs (it was also nontrivial to get working with the JWTs since I'd have to
read the claims, which I don't do anywhere else in the application).
Therefore, it can be deleted, as every deleted endpoint reduces the attack surface of the application.

### Input validation

The vulnerapp now does input validation, limiting the size of posts and checking that the user doesn't use
obviously insecure passwords. This increases the security of the application by protecting against DoS from overly
long text and making it harder to use insecure passwords.

The validation is not very strict and does not check the kinds of characters. That's because this is a blog
platform and the only text we have are blog contents/titles and names. Imposing any sort of character limitations on
either of those things is a bad idea. It's also not needed, as it has been ensured that all data in the vulnerapp
is properly passed through out-of-band (like `node.textContent` and Hibernate queries), preventing injection attacks.

### RBAC

Role based access control is used to limit specific actions. For example, only admins can edit users and only posters
can create posts. If you see that too many people are posting bad computer takes, you can simply take away their
poster role (this has to be done in the database as no API for it exists).

### XSS

The frontend uses DOM API manipulation apis and `textContent` to create the posts, making them immune to XSS.
Interestingly, I wasn't able to get XSS execution when testing on Firefox. The script wasn't shown and the dev
tools showed it as code, but I did not observe its side effects. But with my fix, the script is shown as text.

### Password hashing

The password is hashed using a delegating password encoding, bcrypt by default. This makes sure that the plaintext
password cannot be stolen.

## Other Improvements

There are also two UI changes in the frontend. Error messages from validation/login are now shown as bootstrap
alerts and the creation dates are rendered in a nicer format. It would be even better if the frontend also showed the
cause of the validation error, but spring boot doesn't return it by default.

## Development

There are also a few improvements that don't necessarily increase the security of the vulnerapp but make it nicer
to work with it.

### GitHub Actions CI

There is a GitHub Actions CI pipeline that runs `./gradlew test`. This ensures that the application passes all
the tests. The test report is also uploaded as
a [Github Actions Artifact](https://docs.github.com/en/actions/using-workflows/storing-workflow-data-as-artifacts)
for further inspection if desired. There's also a small badge in the README.

### nix devshell

To allow NixOS users (me!) to work on the vulnerapp. No nix build has been set up because gradle is
quite hard to build and there is currently no nice package to build gradle projects.
Gradle is also just a mess, quite the opposite of reproducibility. You would think that these days a serious
build system like gradle would have lock files, but apparently this technology has not arrived in Java land
yet. Maven should be less painful because there's better nix tooling for it, but I haven't bothered to switch.

# Potential Future Improvements

One possible improvement is the addition of vulnerability scanners to the CI workflow to find dependencies
with vulnerabilities and update them.

There are also scanners that can try finding security vulnerabilities in code. It may also be worth trying out of
them and see whether they find anything from the original vulnerapp. If they do, integrating them into the CI pipeline
could make sense as well.

Instead of rolling our own login system, we could also use OAuth with
an external platform like GitHub. The login system is quite bad with no way to recover passwords, no two-factor
authentication and other more advanced security mechanisms. I'd rather not roll my own complicated login system if
I wanted to put the vulnerapp into production.

The frontend could use a frontend framework like React to fully prevent XSS in the future too without the developer
having to remember to use `textContent` (unless the developer decides to use `dangerouslySetInnerHtml` on potentially
untrusted data, which has happened before on an application I've worked on...).

Not really a security improvement but it would be a sanity improvement to not write the application in Java.
The Java toolchain ecosystem is way too complicated with all the SDKs and problems that come with them (at least the
nix flake mostly solves that, although intellij doesn't always corporate). The language and Spring are also just not
that much fun.

# Problems

Working with spring security was not very nice. Thanks to the deep magic behind it all, misconfigurations were
very hard to debug, even with the highest level of `TRACE` logging. Security is fundamentally a hard problem and
exposing the configs more to the user is not always a good thing, but the way spring security hides everything
does make it hard.
