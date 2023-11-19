﻿import axios from "axios";
import redirect from "react-router-dom/es/Redirect";
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
        // return axios.get(
        //     SHORT_LINK_API + "/",
        //     {withCredentials: true}
        // )
    }
}

export default new ShortLinkService()