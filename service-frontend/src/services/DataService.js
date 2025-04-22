import axios from 'axios';

const STAT_LINK_API = "http://localhost:8080/rest/statistics";

class DataService {
    /**
     * Fetch statistics using the GetStatisticsDto params:
     *   amount       – max number of records
     *   startDate    – ISO string, e.g. "2025-04-22T06:24:30.047266Z"
     *   endDate      – ISO string
     *   hash         – optional short‐link hash
     */
    getStatistics = async ({ amount, startDate, endDate, hash } = {}) => {
        return axios.get(`${STAT_LINK_API}/`, {
            withCredentials: true,
            params: {
                amount,
                startDate,
                endDate,
                hash
            }
        });
    };
}

export default new DataService();
