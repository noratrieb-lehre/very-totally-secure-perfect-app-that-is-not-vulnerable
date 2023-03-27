// globals (yay vanilla javascript ftw)
fetchBlogs();
loginCheck();
document.getElementById("login-form").addEventListener("submit", onLoginSubmit);
document.getElementById("logout-form").addEventListener("submit", onLogoutSubmit);
document.getElementById("blog-form").addEventListener("submit", onBlogSubmit);

function onLoginSubmit(event) {
    const username = event.target[0].value;
    const password = event.target[1].value;
    event.preventDefault();
    fetch("/api/user/whoami", {
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Basic " + btoa(username + ":" + password),
        },
    }).then(filterOk)
        .then(response => response.json())
        .then(user => window.sessionStorage.setItem("fullname", user.fullname))
        .then(() => loginCheck());
}

function onLogoutSubmit(event) {
    event.preventDefault();
    fetch("/logout")
        .then(() => window.sessionStorage.removeItem("fullname"))
        .then(() => loginCheck());
}

function onBlogSubmit(event) {
    const data = {"title": event.target[0].value, "body": event.target[1].value}
    event.preventDefault();
    fetch("/api/blog", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    }).then(filterOk)
        .then(() => fetchBlogs())
        .then(() => event.target.reset())
}

// switch display based on login status
function loginCheck() {
    const fullname = window.sessionStorage.getItem("fullname") || "anonymous";
    let authentic = fullname !== "anonymous"
    document.getElementById("login-form").parentElement.hidden = authentic;
    document.getElementById("logout-form").parentElement.hidden = !authentic;
    document.getElementById("username").innerText = fullname;
}

function fetchBlogs() {
    fetch("/api/blog")
        .then(filterOk)
        .then(response => response.json())
        .then(page => renderBlogs(page.content));
}

function renderBlogs(blogs) {
    const blogDiv = document.getElementById("blog-container");
    blogDiv.innerHTML = "" // clear
    for (const blog of blogs) {
        blogDiv.innerHTML += `<h2>${blog.title}</h2>
            <p>${blog.createdAt}</p>
            <p>${blog.body}</p>`;
    }
}

function filterOk(response) {
    if(response.ok) {
        return response;
    }
    return Promise.reject(response);
}
