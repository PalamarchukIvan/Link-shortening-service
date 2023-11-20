import axios from 'axios';

const SHORT_LINK_API = "http://localhost:8080/rest/statistics";

class DataService {
    async getCurrentUserAllStats() {
        return axios.get(
            SHORT_LINK_API + "/",
            { withCredentials: true }
        );
    }

    async getFilteredDataWithHash(startDate, endDate, hash, amount) {
        return axios.get(
            SHORT_LINK_API + "/hash",
            {
                withCredentials: true,
                params: { startDate, endDate, hash, amount }
            }
        );
    }

    async getFilteredData(startDate, endDate, amount) {
        return axios.get(
            SHORT_LINK_API + "/",
            {
                withCredentials: true,
                params: { startDate, endDate, amount }
            }
        );
    }
}

export default new DataService();