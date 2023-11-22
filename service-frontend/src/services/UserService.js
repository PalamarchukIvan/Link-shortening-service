import axios from "axios";
const USER_API = "http://localhost:8080/rest/reg-log"
class UserService {
    doLogin(user) {
        return axios.post(USER_API + "/login", user, {withCredentials: true})
    }    
    getCurrentUser() {
        return axios.get(USER_API + "/current", {withCredentials: true})
    }
    updateCurrentUser(user) {
        return axios.patch(USER_API + "/update", user, {withCredentials: true})
    }
}

export default new UserService()