﻿import axios from 'axios';

const STAT_LINK_API = "http://localhost:8080/rest/statistics";
const ADMIN_STAT_LINK_API = "http://localhost:8080/rest/admin-statistics";

class DataService {
    async getCurrentUserAllStats() {
        return axios.get(
            STAT_LINK_API + "/",
            { withCredentials: true }
        );
    }

    async getFilteredDataWithHash(startDate, endDate, hash, amount) {
        return axios.get(
            STAT_LINK_API + "/hash",
            {
                withCredentials: true,
                params: { startDate, endDate, hash, amount }
            }
        );
    }

    async getFilteredData(startDate, endDate, amount) {
        return axios.get(
            STAT_LINK_API + "/",
            {
                withCredentials: true,
                params: { startDate, endDate, amount }
            }
        );
    }
    
    
    async getAllUsersUnfilteredStatData() {
        return axios.get(
            ADMIN_STAT_LINK_API + "/",
            {
                withCredentials: true
            }
        );
    }
    async getAllUsersFilteredStatData() {
        return axios.get(
            ADMIN_STAT_LINK_API + "/",
            {
                withCredentials: true
            }
        );
    }
    async getAllUsersFilteredStatDataWithHash() {
        return axios.get(
            ADMIN_STAT_LINK_API + "/",
            {
                withCredentials: true
            }
        );
    }
}

export default new DataService();