import React, { Component } from 'react';
import ShortLinkService from '../services/ShortLinkService';

const SHORT_LINK_REDIRECT_API = 'http://localhost:8080/s/';

class ShortLinksComponent extends Component {
    state = {
        shortLinks: [],
        loading: true,
        error: null,
    };

    async componentDidMount() {
        this.fetchShortLinks();
    }

    fetchShortLinks = async () => {
        this.setState({ loading: true, error: null });
        try {
            const res = await ShortLinkService.getCurrentUserShortLinks();
            this.setState({ shortLinks: res.data.body, loading: false });
        } catch {
            this.setState({ error: 'Failed to load links.', loading: false });
        }
    };

    handleDelete = async (hash) => {
        if (!window.confirm('Are you sure you want to delete this link?')) return;
        try {
            await ShortLinkService.deleteShortLink(hash);
            this.fetchShortLinks();
        } catch {
            this.setState({ error: 'Failed to delete link.' });
        }
    };

    goToCreate = () => {
        window.location.href = '/create-short-link';
    };

    render() {
        const { shortLinks, loading, error } = this.state;

        return (
            <div className="container mt-4">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h3>Your Shortened Links</h3>
                    <button
                        className="btn btn-primary"
                        onClick={this.goToCreate}
                    >
                        + New Short Link
                    </button>
                </div>

                {loading && <div className="text-center py-5">Loading...</div>}
                {error && <div className="alert alert-danger">{error}</div>}

                {!loading && !error && (
                    <div className="table-responsive">
                        <table className="table table-hover table-bordered">
                            <thead className="thead-light">
                            <tr>
                                <th className="text-center">Short Link</th>
                                <th className="text-center">Original URL</th>
                                <th className="text-center">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {shortLinks.map(({ hash, link }) => (
                                <tr key={hash}>
                                    <td className="text-center align-middle">
                                        <a
                                            href={`${SHORT_LINK_REDIRECT_API}${hash}`}
                                            target="_blank"
                                            rel="noopener noreferrer"
                                            className="text-primary font-weight-bold"
                                        >
                                            {SHORT_LINK_REDIRECT_API + hash}
                                        </a>
                                    </td>
                                    <td className="align-middle">{link}</td>
                                    <td className="text-center align-middle">
                                        <button
                                            className="btn btn-sm btn-danger"
                                            onClick={() => this.handleDelete(hash)}
                                        >
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            {shortLinks.length === 0 && (
                                <tr>
                                    <td colSpan="3" className="text-center py-4">
                                        You have no shortened links yet.
                                    </td>
                                </tr>
                            )}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        );
    }
}

export default ShortLinksComponent;
