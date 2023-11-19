import React, {Component} from 'react';
import DataService from "../services/DataService";
import UserService from "../services/UserService";

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
        };
    }

    async componentDidMount() {
        await this.getCurrentUnFilteredStats();
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

    convertMillisecondsToDateTime = (millis) => {
        if(millis == null || millis.toString() == '') {
            console.log("out")
            return ''
        }
        const formattedDate = (millis.toString() / 3600000).toFixed(0) + ':' + (millis.toString() / 60000 % 60).toFixed(0) + ':' + (millis.toString() % 60000 / 1000).toFixed(0);
        return formattedDate;
    };

    formatTime = (time) => {
        // const formattedDate = time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds()
        
        return time.replace('T', ' ').replace('Z', ' ').split('.')[0]
    };
    
    render() {
        return (
            <div>
                <h2>Statistics Table</h2>
                <table className="table">
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
                            <td>{index + 1 }</td>
                            <td>{this.formatTime(statistic.time)}</td>
                            <td>{statistic.hash}</td>
                            {/*<td>{statistic.expectedDuration}</td>*/}
                            <td>{this.convertMillisecondsToDateTime(statistic.expectedDuration)}</td>
                            <td>{statistic.exists.toString()}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        );
    }
}

export default StatisticsComponent;