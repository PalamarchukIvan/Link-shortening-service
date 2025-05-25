import React, { Component } from 'react';
import DataService from '../services/DataService';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { Chart, registerables } from 'chart.js';
import { Bar } from 'react-chartjs-2';

Chart.register(...registerables);

export default class StatisticsComponent extends Component {
    state = {
        statistics: [],
        startDate: null,
        endDate: null,
        filterHash: '',
        filterNumRecords: '',
        chartData: null,
        showModal: false,
        modalDate: null,
        modalHash: null,
        modalEntries: []
    };

    componentDidMount() {
        this.loadStatistics();
    }

    // Load unfiltered or filtered based on state
    loadStatistics = async () => {
        const { filterHash, filterNumRecords, startDate, endDate } = this.state;
        try {
            const res = await DataService.getStatistics({
                hash: filterHash || null,
                amount: filterNumRecords || null,
                startDate: startDate ? startDate.toISOString() : null,
                endDate: endDate ? endDate.toISOString() : null
            });
            this.setState({ statistics: res.data.body }, this.prepareChart);
        } catch (err) {
            console.error('Error fetching statistics:', err);
        }
    };

    prepareChart = () => {
        const { statistics } = this.state;
        const buckets = {};
        statistics.forEach(s => {
            const raw = s.visitTime;
            const fixedIso = raw.replace(/\.(\d{3})\d*Z$/, '.$1Z');
            const d = new Date(fixedIso);
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

    handleFilterChange = e => this.setState({ [e.target.name]: e.target.value });

    handleFilterSubmit = e => {
        e.preventDefault();
        console.log('Applying filters');
        this.loadStatistics();
    };

    onChartClick = (evt, elements) => {
        if (!elements.length) return;
        const { chartData, statistics } = this.state;
        const el = elements[0];
        const date = chartData.labels[el.index];
        const hash = chartData.datasets[el.datasetIndex].label;
        const entries = statistics.filter(
            s => s.hash === hash && s.visitTime.slice(0, 10) === date
        );
        this.setState({ showModal: true, modalDate: date, modalHash: hash, modalEntries: entries });
    };

    closeModal = () => this.setState({ showModal: false });

    renderModal = () => {
        const { showModal, modalDate, modalHash, modalEntries } = this.state;
        if (!showModal) return null;

        return (
            <div
                className="modal-backdrop"
                style={{ backdropFilter: 'blur(5px)', backgroundColor: 'rgba(255,255,255,0.6)' }}
            >
                <div className="modal-dialog">
                    <div className="modal-content" style={{ color: '#000' }}>
                        <div className="modal-header">
                            <h5 className="modal-title">
                                Visits for {modalHash} on {modalDate}
                            </h5>
                            <button type="button" className="btn-close" onClick={this.closeModal} />
                        </div>
                        <div className="modal-body">
                            <table className="table">
                                <thead>
                                <tr><th>#</th><th>Time</th><th>Found</th></tr>
                                </thead>
                                <tbody>
                                {modalEntries.map((e, i) => (
                                    <tr key={i}>
                                        <td>{i + 1}</td>
                                        <td>{new Date(e.visitTime.slice(0, 23) + 'Z').toLocaleTimeString()}</td>
                                        <td>{e.found ? 'Yes' : 'No'}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" onClick={this.closeModal}>
                                Close
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        );
    };

    render() {
        const { chartData, startDate, endDate, filterHash, filterNumRecords } = this.state;
        return (
            <div className="container mt-4">
                <h2 className="mb-4">Usage Histogram</h2>

                <form onSubmit={this.handleFilterSubmit} className="row g-3 mb-4">
                    <div className="col-md-3">
                        <label htmlFor="filterHash" className="form-label">Filter by Hash</label>
                        <input
                            type="text"
                            id="filterHash"
                            name="filterHash"
                            value={filterHash}
                            className="form-control"
                            onChange={this.handleFilterChange}
                        />
                    </div>
                    <div className="col-md-2">
                        <label htmlFor="filterNumRecords" className="form-label"># Records</label>
                        <input
                            type="number"
                            id="filterNumRecords"
                            name="filterNumRecords"
                            value={filterNumRecords}
                            className="form-control"
                            onChange={this.handleFilterChange}
                        />
                    </div>
                    <div className="col-md-2">
                        <label className="form-label">Start Date</label>
                        <DatePicker selected={startDate} onChange={date => this.setState({ startDate: date })} className="form-control" />
                    </div>
                    <div className="col-md-2">
                        <label className="form-label">End Date</label>
                        <DatePicker selected={endDate} onChange={date => this.setState({ endDate: date })} className="form-control" />
                    </div>
                    <div className="col-md-12 text-end">
                        <button type="submit" className="btn btn-primary">Apply Filters</button>
                    </div>
                </form>

                {chartData && (
                    <Bar
                        data={chartData}
                        options={{ responsive: true, onClick: this.onChartClick, scales: { x: { stacked: true }, y: { stacked: true, beginAtZero: true } } }}
                    />
                )}

                {this.renderModal()}
            </div>
        );
    }
}
