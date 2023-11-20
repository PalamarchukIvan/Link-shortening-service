import axios from "axios";
const SHORT_LINK_API = "http://localhost:8080/rest/short-links"
class ShortLinkService {
    async getCurrentUserShortLinks() {
        return axios.get(
            SHORT_LINK_API + "/",
            {withCredentials: true}
        )
    }
    createShortLink(shortLink) {
        return axios.post(SHORT_LINK_API + "/create-short-link",
                   shortLink,
            {withCredentials: true})
    }
    deleteShortLink(hash = "") {
        return axios.delete(SHORT_LINK_API + "/delete/" + hash,
            {withCredentials: true})
    }
}

export default new ShortLinkService()