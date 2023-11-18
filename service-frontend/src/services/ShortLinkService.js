import axios from "axios";
const SHORT_LINK_API = "http://localhost:8080/rest/short-links"
class ShortLinkService {
    getCurrentUserShortLinks() {
        return axios.get(SHORT_LINK_API + "/")
    }
}

export default new ShortLinkService()