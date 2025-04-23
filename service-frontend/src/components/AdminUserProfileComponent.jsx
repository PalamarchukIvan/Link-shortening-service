import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import UserService from '../services/UserService';

class AdminUserProfileComponent extends Component {
    state = {
        user: null,
        loading: true,
        error: null,
        // pagination state
        page: 0,
        pageSize: 10
    };

    async componentDidMount() {
        const params = new URLSearchParams(this.props.location.search);
        const login = params.get('login');
        if (!login) {
            this.setState({error: 'No login specified', loading: false});
            return;
        }

        try {
            const res = await UserService.getUser(login);
            this.setState({user: res.data.body, loading: false});
        } catch (err) {
            console.error(err);
            this.setState({error: 'Failed to load user', loading: false});
        }
    }

    handlePrev = () => this.setState(({page}) => ({page: Math.max(page - 1, 0)}));
    handleNext = () => this.setState(({page, pageSize, user}) => ({
        page: Math.min(page + 1, Math.ceil(user.links.length / pageSize) - 1)
    }));

    render() {
        const {user, loading, error, page, pageSize} = this.state;
        if (loading) return <div className="p-4">Loading...</div>;
        if (error) return <div className="alert alert-danger m-4">{error}</div>;
        if (!user) return null;

        const isAdmin = user.role.includes('ADMIN');
        const start = page * pageSize;
        const end = start + pageSize;
        const pageLinks = user.links.slice(start, end);

        return (
            <div className="container mt-4">
                <div className="row">
                    {/* Left panel: user info */}
                    <div className="col-md-4 mb-4">
                        <div className="card h-100">
                            <div className="card-header d-flex justify-content-between align-items-center">
                                <h4 className="mb-0">Profile</h4>
                                <span
                                    className={`badge ${isAdmin ? 'bg-warning' : 'bg-secondary'}`}>{isAdmin ? 'ADMIN' : 'USER'}</span>
                            </div>
                            <div className="card-body">
                                <div className="mb-3">
                                    <label className="form-label">Name</label>
                                    <input className="form-control" readOnly value={user.name}/>
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Login</label>
                                    <input className="form-control" readOnly value={user.username}/>
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Active</label>
                                    <input className="form-control" readOnly value={user.isActive ? 'Yes' : 'No'}/>
                                </div>
                                <div>
                                    <label className="form-label">Total Links</label>
                                    <input className="form-control" readOnly value={user.links.length}/>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Right panel: links table */}
                    <div className="col-md-8">
                        <h5>User’s Short Links</h5>
                        <div style={{maxHeight: '60vh', overflowY: 'auto'}}>
                            <table className="table table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Hash</th>
                                    <th>URL</th>
                                    <th>Deleted?</th>
                                    <th>Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                {pageLinks.map((l, idx) => (
                                    <tr key={l.hash} className={l.deleted ? 'table-danger' : ''}>
                                        <td>{start + idx + 1}</td>
                                        <td>{l.hash}</td>
                                        <td><a href={l.link} target="_blank" rel="noopener noreferrer">{l.link}</a></td>
                                        <td>{l.deleted ? 'Yes' : 'No'}</td>
                                        <td>
                                            <button className="btn btn-sm btn-info"
                                                    onClick={() => window.location.href = `/all-users-statistic?hash=${l.hash}&login=${user.username}`}>View
                                                Stats
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                                {user.links.length === 0 && (
                                    <tr>
                                        <td colSpan="5" className="text-center py-3">No links found.</td>
                                    </tr>
                                )}
                                </tbody>
                            </table>
                        </div>

                        {user.links.length > pageSize && (
                            <div className="d-flex justify-content-between my-2">
                                <button className="btn btn-secondary" onClick={this.handlePrev}
                                        disabled={page === 0}>Previous
                                </button>
                                <button className="btn btn-secondary" onClick={this.handleNext}
                                        disabled={end >= user.links.length}>Next
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(AdminUserProfileComponent);