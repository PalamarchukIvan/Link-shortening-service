import React, { Component } from 'react';
import DataService from '../services/DataService';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import {Bar, Line} from 'react-chartjs-2';
import {Chart, ChartData, ChartDataset, DefaultDataPoint, registerables } from 'chart.js';
import {useParams} from "react-router-dom";

class AdminStatisticsComponent extends Component {
    constructor(props) {
        super(props);
        Chart.register(...registerables)
        this.state = {
            statistics: [
                {
                    columnNumber: '',
                    time: '',
                    hash: '',
                    expectedDuration: '',
                    exists: '',
                    user: {
                         id: '',
                         name: '',
                         username: '',
                         role: '',
                         password: '',
                         isActive: ''
                    }
                },
            ],
            endDate: null,
            startDate: null,
            filterHash: '',
            filterNumRecords: null,
            filterUsername: '',
            chartInstance: null, 
        };
    }

    async getCurrentUnFilteredStats() {
        try {
            const res = await DataService.getAllUsersUnfilteredStatData();
            if (res.config.url !== res.request.responseURL) {
                document.location = res.request.responseURL;
            } else {
                this.setState({
                    statistics: res.data,
                });
            }
        } catch (error) {
            console.error('Error fetching statistics:', error);
        }
    }
    

    async componentDidMount() {
        await this.getCurrentUnFilteredStats();
    }


    convertMillisecondsToDateTime = (millis) => {
        if (millis == null || millis.toString() === '') {
            return '';
        }
        const formattedDate =
            (millis.toString() / 3600000).toFixed(0) +
            ':' +
            (millis.toString() / 60000 % 60).toFixed(0) +
            ':' +
            (millis.toString() % 60000 / 1000).toFixed(0);
        return formattedDate;
    };

    formatTime = (time) => {
        return time.replace('T', ' ').replace('Z', ' ').split('.')[0];
    };

    handleFilterChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value,
        });
    };

    handleFilterSubmit = async (event) => {
        event.preventDefault();

        try {
            const res = await DataService.getAllUsersFilteredStatData(
                            this.state.filterHash,
                            this.state.filterNumRecords,
                            this.state.filterUsername,
                            this.getDateISOString(this.state.startDate),
                            this.getDateISOString(this.state.endDate)
                    )

            if (res.request.responseURL.includes('/login')) {
                document.location = res.request.responseURL;
            } else {
                console.log(res.data)
                this.setState({
                    statistics: res.data,
                });
            }
        } catch (error) {
            console.error('Error fetching filtered data:', error);
        }
    };

    getDateISOString = (date) =>
        date instanceof Date && !isNaN(date) ? date.toISOString() : null;

    renderChart = () => {
        const uniqueHashes = [...new Set(this.state.statistics.map((statistic) => statistic.hash))];
        const filteredData = uniqueHashes.map((hash) => {
            const lastEntry = this.state.statistics
                .filter((statistic) => statistic.hash === hash)
                .pop(); // Get the last entry for each hash
            return lastEntry;
        });
        
        const lineChartData = {
            labels: filteredData.map((statistic) => statistic.hash),
            datasets: [
                {
                    label: 'Expected Duration',
                    data: filteredData
                    .map((statistic) =>
                        statistic.expectedDuration / 3600000
                    ),
                    backgroundColor: 'rgba(75, 192, 192, 0.6)',
                    borderWidth: 2,
                },
            ],
        };

        return (
                <Bar
                    ref={(chart) => {
                        this.state.chartInstance = chart ? chart.chartInstance : null;
                    }}
                    data={lineChartData}
                    options={{
                        scales: {
                            x: {
                                type: 'category',
                                labels: filteredData.map((statistic) => statistic.hash),
                            },
                            y: {
                                beginAtZero: true,
                            },
                        },
                    }}
                />
        );
    };

    render() {

        console.log(this.state.statistics)
        return (
            <div>
                <h2 className="mb-4">Statistics Table</h2>
                <div style={{ maxHeight: '400px', overflowY: 'auto' }}>
                    <table className="table mt-4">
                        <thead>
                        <tr>
                            <th>Column Number</th>
                            <th>Login</th>
                            <th>Visit Time</th>
                            <th>Visited Site Hash</th>
                            <th>Expected Duration</th>
                            <th>Is found</th>
                        </tr>
                        </thead>
                        <tbody>
                        {this.state.statistics.map((statistic, index) => (
                            <tr key={index}>
                                <td>{index + 1}</td>
                                <td> <a href={"/user-stat/profile?login=" + statistic.user.username } className={"text-black"} style={{ textDecoration: 'none' }}>{statistic.user.username}</a> </td>
                                <td>{this.formatTime(statistic.time)}</td>
                                <td>{statistic.hash}</td>
                                <td>{statistic.expectedDuration != 0 ? this.convertMillisecondsToDateTime(statistic.expectedDuration) : 'is not finished'}</td>
                                <td>{statistic.exists.toString()}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
                <br />
                <form onSubmit={this.handleFilterSubmit} className="mb-3">
                    <div className="row">
                        <div className="col-md-3">
                            <label htmlFor="filterHash" className="form-label">Filter by Hash:</label>
                            <input type="text" className="form-control" id="filterHash" name="filterHash" value={this.state.filterHash} onChange={this.handleFilterChange} />
                        </div>
                        <div className="col-md-3">
                            <label htmlFor="filterUsername" className="form-label">Filter By username:</label>
                            <input type="text" className="form-control" id="filterUsername" name="filterUsername" value={this.state.filterUsername} onChange={this.handleFilterChange} />
                        </div>
                        <div className="col-md-3">
                            <label htmlFor="filterNumRecords" className="form-label">Filter by Number of Records:</label>
                            <input type="number" className="form-control" id="filterNumRecords" name="filterNumRecords" value={this.state.filterNumRecords} onChange={this.handleFilterChange} />
                        </div>
                        <span> </span>
                        <br/>
                        <div className="col-md-3">
                            <label className="form-label">Filter by Start Date:</label>
                            <DatePicker
                                selected={this.state.startDate}
                                onChange={(date) => this.setState( {
                                    startDate: date
                                })
                                }
                                className="form-control"
                            />
                        </div>
                        <div className="col-md-3">
                            <label className="form-label">Filter by End Date:</label>
                            <DatePicker
                                selected={this.state.endDate}
                                onChange={(date) => this.setState({
                                    endDate: date
                                })
                                }
                                className="form-control"
                            />
                        </div>
                    </div>
                    <button type="submit" className="btn btn-primary mt-3">Apply Filters</button>
                </form>

                <div >
                    <div className="mt-4">
                        <h2>Expected Duration Over Time</h2>
                        {this.renderChart()}
                    </div>
                    <br />
                    <br />
                </div>
            </div>

        );
    }
}

export default AdminStatisticsComponent;
