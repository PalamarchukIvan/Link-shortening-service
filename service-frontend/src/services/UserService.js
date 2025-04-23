import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080/rest/user",
    withCredentials: true,       // all calls carry cookies
});

export default {
    doLogin: creds => api.post("/login", creds),
    doRegistration: creds => api.post("/registration", creds),
    getCurrentUser: () => api.get("/current"),
    updateCurrentUser: user => api.patch("/update", user),
    verify: token => api.post("/verify", null, {params: {token}}),
    getUser: login => api.get("/profile", {params: {login}})
}
