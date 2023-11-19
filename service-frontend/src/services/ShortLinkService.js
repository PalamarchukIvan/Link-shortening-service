import axios from "axios";
import redirect from "react-router-dom/es/Redirect";
const SHORT_LINK_API = "http://localhost:8080/rest/short-links"
class ShortLinkService {
    async getCurrentUserShortLinks() {
        // return axios.get(SHORT_LINK_API + "/")
        return await fetch(
            SHORT_LINK_API + "/",
            {method: 'GET', redirect: 'follow', credentials: 'include'}
        )
    }
    createShortLink(shortLink) {
        return axios.post(SHORT_LINK_API + "/create-short-link", shortLink)
    }
}

export default new ShortLinkService()