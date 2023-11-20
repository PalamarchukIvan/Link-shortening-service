﻿import React, {Component, useState} from 'react';
import DataService from "../services/DataService";
import UserService from "../services/UserService";
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";
import moment from 'moment';

class StatisticsComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            statistics: [
                {
                  columnNumber: '', 
                  time: '', 
                  hash: '', 
                  expectedDuration: '', 
                  exists: ''
                }
            ],
            endDate: null,
            startDate: null,
            filterHash: '',
            filterNumRecords: null
        };
    }

    async componentDidMount() {
        await this.getCurrentUnFilteredStats();

        this.updateLastRowInterval = setInterval(this.updateLastRow, 1000);
    }
    componentWillUnmount() {
        clearInterval(this.updateLastRowInterval);
    }
    async getCurrentUnFilteredStats() {
        const res = await DataService.getCurrentUserAllStats().then(res => {
            return res
        })
        // console.log(res)
        if (res.config.url !== res.request.responseURL) {
            // console.log(res)
            document.location = res.request.responseURL
        } else {
            console.log('data => ')
            this.setState({
                statistics: res.data
            })
            console.log(this.state.statistics)
        }
    }
    updateLastRow = () => {
        const { statistics } = this.state;

        if (statistics.length > 0) {
            const lastRow = statistics[statistics.length - 1];
            const startTime = new Date(lastRow.time);
            let timeDiff = null
            const currentTime = new Date();
            if(statistics.length > 1 && statistics[statistics.length - 2].hash === lastRow.hash) {
                const preLastRow = statistics[statistics.length - 2];
                timeDiff = currentTime - startTime + preLastRow.expectedDuration
            } else{
                timeDiff = currentTime - startTime;
            }

            this.setState((prevState) => ({
                statistics: [
                    ...prevState.statistics.slice(0, -1),
                    {
                        ...lastRow,
                        expectedDuration: timeDiff
                    }
                ]
            }));
            console.log(lastRow)
        }
    };
    
    convertMillisecondsToDateTime = (millis) => {
        if(millis == null || millis.toString() == '') {
            console.log("out")
            return ''
        }
        const formattedDate = (millis.toString() / 3600000).toFixed(0) + ':' + (millis.toString() / 60000 % 60).toFixed(0) + ':' + (millis.toString() % 60000 / 1000).toFixed(0);
        return formattedDate;
    };

    formatTime = (time) => {        
        return time.replace('T', ' ').replace('Z', ' ').split('.')[0]
    };
    
    handleFilterChange = (event)=> {
        this.setState({
            [event.target.name]: event.target.value,
        });
    }
    handleFilterSubmit = (event) => {
        event.preventDefault();
        
        if(this.state.filterHash != null && this.state.filterHash != '') {
            console.log("with hash")
            console.log(
                this.state.startDate,
                this.state.endDate,
                this.state.filterHash,
                this.state.filterNumRecords)
            const res = DataService.getFilteredDataWithHash(
                (this.state.startDate instanceof Date && !isNaN(this.state.startDate)) ? this.state.startDate.toISOString() : null,
                (this.state.endDate instanceof Date && !isNaN(this.state.endDate)) ? this.state.endDate.toISOString() : null,
                this.state.filterHash,
                this.state.filterNumRecords
            ).then(res => {
                console.log(res)
                if (res.request.responseURL.includes('login')) {
                    document.location = res.request.responseURL
                    // console.log(res.request.responseURL)
                } else {
                    console.log('data => ')
                    this.setState({
                        statistics: res.data
                    })
                    console.log(this.state.statistics)
                }
            })
            
        } else {
            console.log("without hash")
            console.log(this.state.startDate,
                this.state.endDate,
                this.state.filterHash,
                this.state.filterNumRecords)
            const res = DataService.getFilteredData(
                (this.state.startDate instanceof Date && !isNaN(this.state.startDate)) ? this.state.startDate.toISOString() : null,
                (this.state.endDate instanceof Date && !isNaN(this.state.endDate)) ? this.state.endDate.toISOString() : null,
                this.state.filterNumRecords
            ).then(res => {
                console.log(res)
                if (res.request.responseURL.includes('login')) {
                    document.location = res.request.responseURL
                    // console.log(res.request.responseURL)
                } else {
                    console.log('data => ')
                    this.setState({
                        statistics: res.data
                    })
                    console.log(this.state.statistics)
                }
            })
            
        }
    }
    render() {
        return (
            <div>
                <h2 className="mb-4">Statistics Table</h2>
                <table className="table mt-4">
                    <thead>
                    <tr>
                        <th>Column Number</th>
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
                            <td>{this.formatTime(statistic.time)}</td>
                            <td>{statistic.hash}</td>
                            <td>{this.convertMillisecondsToDateTime(statistic.expectedDuration)}</td>
                            <td>{statistic.exists.toString()}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <br/>
                <form onSubmit={this.handleFilterSubmit} className="mb-3">
                    <div className="row">
                        <div className="col-md-3">
                            <label htmlFor="filterHash" className="form-label">Filter by Hash:</label>
                            <input type="text" className="form-control" id="filterHash" name="filterHash" value={this.state.filterHash} onChange={this.handleFilterChange} />
                        </div>
                        <div className="col-md-3">
                            <label htmlFor="filterNumRecords" className="form-label">Filter by Number of Records:</label>
                            <input type="number" className="form-control" id="filterNumRecords" name="filterNumRecords" value={this.state.filterNumRecords} onChange={this.handleFilterChange} />
                        </div>
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
            </div>
        );
    }
}

export default StatisticsComponent;