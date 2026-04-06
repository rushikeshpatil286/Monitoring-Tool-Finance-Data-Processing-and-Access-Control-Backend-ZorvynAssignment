function apiGet(url) {
    return fetch(url, { credentials: "same-origin" })
        .then(res => res.json());
}

function apiPost(url, data) {
    return fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "same-origin",
        body: JSON.stringify(data)
    }).then(res => res.json());
}
