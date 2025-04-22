import React, { Component } from 'react';
import ShortLinkService from '../services/ShortLinkService';

const SHORT_LINK_REDIRECT_API = 'http://localhost:8080/s/';

class CreateShortLinkComponent extends Component {
    state = {
        link: '',
        createdHash: '',
        loading: false,
        error: null
    };

    handleChange = (e) => {
        this.setState({ link: e.target.value, error: null });
    };

    createShortLink = async (e) => {
        e.preventDefault();
        const { link } = this.state;
        if (!link.trim()) {
            this.setState({ error: 'Please enter a valid URL.' });
            return;
        }

        this.setState({ loading: true, error: null });
        try {
            const res = await ShortLinkService.createShortLink({ link });
            const hash = res.data.body.hash;
            this.setState({ createdHash: hash, link: '', loading: false });
        } catch (err) {
            this.setState({ error: 'Failed to create short link.', loading: false });
        }
    };

    goBack = () => {
        window.location.href = '/main';
    };

    goToLink = () => {
        const { createdHash } = this.state;
        window.location.href = `${SHORT_LINK_REDIRECT_API}${createdHash}`;
    };

    render() {
        const { link, createdHash, loading, error } = this.state;

        return (
            <div className="container mt-5">
                <div className="card mx-auto" style={{ maxWidth: '500px' }}>
                    <div className="card-body">
                        <h4 className="card-title mb-4 text-center">Create New Short Link</h4>

                        {error && <div className="alert alert-danger">{error}</div>}

                        <form onSubmit={this.createShortLink}>
                            <div className="form-group mb-3">
                                <label htmlFor="linkInput">Original URL</label>
                                <input
                                    id="linkInput"
                                    type="url"
                                    className="form-control"
                                    placeholder="Enter URL to shorten"
                                    value={link}
                                    onChange={this.handleChange}
                                    required
                                />
                            </div>

                            <div className="d-flex justify-content-between">
                                <button
                                    type="submit"
                                    className="btn btn-success"
                                    disabled={loading}
                                >
                                    {loading ? 'Creating...' : 'Save'}
                                </button>
                                <button
                                    type="button"
                                    className="btn btn-secondary"
                                    onClick={this.goBack}
                                >
                                    Go Back
                                </button>
                            </div>
                        </form>

                        {!createdHash ? null : (
                            <div className="alert alert-info mt-4 text-center">
                                <p>Your shortened link:</p>
                                <a
                                    href={`${SHORT_LINK_REDIRECT_API}${createdHash}`}
                                    onClick={(e) => { e.preventDefault(); this.goToLink(); }}
                                    className="font-weight-bold"
                                >
                                    {SHORT_LINK_REDIRECT_API + createdHash}
                                </a>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        );
    }
}

export default CreateShortLinkComponent;
