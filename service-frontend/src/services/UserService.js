import axios from "axios";
const SHORT_LINK_API = "http://localhost:8080"
class UserService {
    doLogin(user) {
        return axios.post(SHORT_LINK_API + "/login", user)
    }
}

export default new UserService()