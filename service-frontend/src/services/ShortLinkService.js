import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080/rest/short-links",
    withCredentials: true,       // all calls carry cookies
});

export default {
    getCurrentUserShortLinks: () => api.get("/"),
    createShortLink: shortLink => api.post("/create", shortLink),
    deleteShortLink: (hash) => api.delete("/delete/" + hash)
}