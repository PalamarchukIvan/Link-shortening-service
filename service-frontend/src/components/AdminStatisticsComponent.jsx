import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import DataService from '../services/DataService';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { Chart, registerables } from 'chart.js';
import { Bar } from 'react-chartjs-2';

Chart.register(...registerables);

class AdminStatisticsComponent extends Component {
    state = {
        login: null,
        filterHash: '',
        filterUsername: '',
        filterNumRecords: '',
        startDate: null,
        endDate: null,
        statistics: [],
        chartData: null,
        showModal: false,
        modalDate: null,
        modalHash: null,
        modalEntries: [],
        
        dataWithLoginFound: false,
        loginUsedToSearch: '',
    };

    componentDidMount() {
        const params = new URLSearchParams(this.props.location.search);
        const login = params.get('login');
        const filterHash = params.get('hash');
        this.setState({ filterUsername: login, filterHash: filterHash }, this.loadStats);
    }

    loadStats = async () => {
        const { filterHash, filterUsername, filterNumRecords, startDate, endDate, login } = this.state;
        try {
            const res = await DataService.getStatisticsAdmin({
                    hash: filterHash || null,
                    amount: filterNumRecords || null,
                    login: filterUsername  || '',
                    startDate: startDate ? startDate.toISOString() : null,
                    endDate: endDate ? endDate.toISOString() : null
                }
            );
            this.setState({ statistics: res.data.body }, this.prepareChart);
            if (filterUsername !== '' && res.data.body.length !== 0) {
                this.setState( {dataWithLoginFound: true, loginUsedToSearch: filterUsername } )
            } else {
                this.setState( {dataWithLoginFound: false } )
            }
        } catch (err) {
            console.error('Error loading statistics:', err);
        }
    };

    prepareChart = () => {
        const { statistics } = this.state;
        const buckets = {};
        statistics.forEach(s => {
            const fixed = s.visitTime.replace(/\.(\d{3})\d*Z$/, '.$1Z');
            const d = new Date(fixed);
            if (isNaN(d.getTime())) return;
            const dateKey = d.toISOString().slice(0, 10);
            buckets[dateKey] = buckets[dateKey] || {};
            buckets[dateKey][s.hash] = (buckets[dateKey][s.hash] || 0) + 1;
        });

        const dates = Object.keys(buckets).sort();
        const hashes = [...new Set(statistics.map(s => s.hash))];
        const datasets = hashes.map((hash, idx) => ({
            label: hash,
            data: dates.map(d => buckets[d][hash] || 0),
            backgroundColor: `hsl(${(idx * 60) % 360},70%,60%)`
        }));

        this.setState({ chartData: { labels: dates, datasets } });
    };

    handleChange = e => this.setState({ [e.target.name]: e.target.value });
    handleDateChange = (field, date) => this.setState({ [field]: date });
    handleFilterSubmit = e => { e.preventDefault(); this.loadStats(); };

    onChartClick = (_, elements) => {
        if (!elements.length) return;
        const { chartData, statistics, login, filterUsername } = this.state;
        const { index, datasetIndex } = elements[0];
        const date = chartData.labels[index];
        const hash = chartData.datasets[datasetIndex].label;
        const entries = statistics.filter(s =>
            s.hash === hash && s.visitTime.slice(0,10) === date
        );
        this.setState({ showModal: true, modalDate: date, modalHash: hash, modalEntries: entries });
    };

    closeModal = () => this.setState({ showModal: false });

    renderModal() {
        const { showModal, modalDate, modalHash, modalEntries } = this.state;
        if (!showModal) return null;

        return (
            <div className="modal-backdrop" style={{ backdropFilter: 'blur(4px)', backgroundColor: 'rgba(255,255,255,0.5)' }}>
                <div className="modal-dialog modal-lg">
                    <div className="modal-content text-dark">
                        <div className="modal-header">
                            <h5 className="modal-title">Visits for hash “{modalHash}” on {modalDate}</h5>
                            <button className="btn-close" onClick={this.closeModal} />
                        </div>
                        <div className="modal-body">
                            <table className="table table-sm">
                                <thead>
                                <tr><th>#</th><th>User</th><th>Time</th><th>Found</th></tr>
                                </thead>
                                <tbody>
                                {modalEntries.map((e,i) => (
                                    <tr key={i} className={!e.found ? 'table-danger' : ''}>
                                        <td>{i+1}</td>
                                        <td>{e.user.username}</td>
                                        <td>{new Date(e.visitTime.replace(/\.(\d{3})\d*Z$/, '.$1Z')).toLocaleTimeString()}</td>
                                        <td>{e.found ? 'Yes' : 'No'}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                        <div className="modal-footer">
                            <button className="btn btn-secondary" onClick={this.closeModal}>Close</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    render() {
        const { login, filterHash, filterUsername, filterNumRecords, startDate, endDate, chartData, dataWithLoginFound, loginUsedToSearch } = this.state;
        return (
            <div className="container mt-4">
                <h3>Statistics {login ? `for ${login}` : 'Overview'}</h3>

                <form onSubmit={this.handleFilterSubmit} className="row gx-3 gy-2 align-items-end mb-4">
                    <div className="col-md-3">
                        <label className="form-label">Filter by Hash</label>
                        <input name="filterHash" value={filterHash} className="form-control" onChange={this.handleChange} />
                    </div>
                    <div className="col-md-3">
                        <label className="form-label">Filter by Username</label>
                        <input name="filterUsername" value={filterUsername} className="form-control" onChange={this.handleChange} />
                    </div>
                    <div className="col-md-2">
                        <label className="form-label"># Records</label>
                        <input name="filterNumRecords" type="number" value={filterNumRecords} className="form-control" onChange={this.handleChange} />
                    </div>
                    <div className="col-md-2">
                        <label className="form-label">Start Date</label>
                        <DatePicker selected={startDate} onChange={d=>this.handleDateChange('startDate',d)} className="form-control"/>
                    </div>
                    <div className="col-md-2">
                        <label className="form-label">End Date</label>
                        <DatePicker selected={endDate} onChange={d=>this.handleDateChange('endDate',d)} className="form-control"/>
                    </div>
                    <div className="col-md-2 d-grid">
                        <button type="submit" className="btn btn-primary">Apply Filters</button>
                    </div>
                    {(dataWithLoginFound) && (
                        <div className="col-md-3 d-grid">
                            <button
                                type="button"
                                className="btn btn-outline-secondary"
                                onClick={()=> window.location.href = `/admin/user-profile?login=${loginUsedToSearch}`}
                            >
                                View User Profile
                            </button>
                        </div>
                    )}
                </form>

                {chartData && (
                    <Bar data={chartData} options={{ onClick: this.onChartClick, scales: { x: { stacked: true }, y: { stacked: true, beginAtZero: true } } }} />
                )}

                {this.renderModal()}
            </div>
        );
    }
}

export default withRouter(AdminStatisticsComponent);
