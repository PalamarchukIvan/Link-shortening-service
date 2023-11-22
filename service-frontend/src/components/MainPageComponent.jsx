import React, { Component } from 'react';
import ShortLinkService from '../services/ShortLinkService';
import { withRouter } from 'react-router-dom';
import UserService from '../services/UserService';

class MainPageComponent extends Component {
    constructor(props) {
        super(props);

        this.createLink = this.createLink.bind(this);
        this.viewStats = this.viewStats.bind(this);
        this.viewAllLinks = this.viewAllLinks.bind(this);
        this.getUserData = this.getUserData.bind(this);
        this.toggleEdit = this.toggleEdit.bind(this);
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.saveChanges = this.saveChanges.bind(this);

        this.state = {
            user: {
                id: '',
                username: '',
                name: '',
                password: '',
                isActive: '',
                links: [],
                role: [],
            },
            editing: false,
            newUsername: '',
        };
    }

    async componentDidMount() {
        console.log('test');
        await this.getUserData();
    }

    async getUserData() {
        const res = await UserService.getCurrentUser().then((res) => {
            return res;
        });
        console.log(res);
        if (res.config.url !== res.request.responseURL) {
            console.log(res);
            document.location = res.request.responseURL;
        } else {
            console.log('data => ' + res.data);
            this.setState({
                user: res.data,
            });
        }
    }

    createLink() {
        console.log('Button clicked! createLink');
    }

    viewStats() {
        console.log('Button clicked! viewStats');
    }

    viewAllLinks() {
        console.log('Button clicked! viewAllLinks');
    }

    toggleEdit() {
        this.setState((prevState) => ({
            editing: !prevState.editing,
            newUsername: prevState.editing ? '' : prevState.user.name,
        }));
    }

    handleUsernameChange(event) {
        this.setState({ newUsername: event.target.value });
    }

    saveChanges() {
        // Update the user in the backend and then update the local state
        UserService.updateCurrentUser({ ...this.state.user, name: this.state.newUsername })
            .then((updatedUser) => {
                this.setState((prevState) => ({
                    editing: false,
                    user: updatedUser.data,
                }));
            })
            .catch((error) => {
                console.error('Error updating user:', error);
            });
    }

    render() {
        return (
            <div className="container mt-5">
                <div className="card">
                    <div className="card-header">
                        <h2>User Profile</h2>
                    </div>
                    <div className="card-body">
                        <div className="form-group">
                            <label htmlFor="name">Name:</label>
                            <div className="input-group">
                                {this.state.editing ? (
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="name"
                                        value={this.state.newUsername}
                                        onChange={this.handleUsernameChange}
                                    />
                                ) : (
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="name"
                                        readOnly
                                        value={this.state.user.name}
                                    />
                                )}
                                <div className="input-group-append">
                                    {this.state.editing ? (
                                        <button className="btn btn-success" onClick={this.saveChanges}>Save</button>) : 
                                        (<button className="btn btn-warning" onClick={this.toggleEdit}>Edit</button>
                                    )}
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="login">Login:</label>
                            <input
                                type="text"
                                className="form-control"
                                id="login"
                                readOnly
                                value={this.state.user.username}
                            />
                        </div>
                        <div className="form-group">
                            <label>Number of Short Links Ever Created:</label>
                            <input
                                type="text"
                                className="form-control"
                                id="short-links"
                                readOnly
                                value={this.state.user.links.length}
                            />
                        </div>
                        <a
                            className="btn btn-primary"
                            href="/create-short-link"
                            onClick={this.createLink}
                        >
                            Create Short Link
                        </a>
                        <a
                            className="btn btn-info"
                            href="/user-stat/statistic"
                            onClick={this.viewStats}
                        >
                            View Link Statistics
                        </a>
                        <a
                            className="btn btn-secondary"
                            href="/short-links"
                            onClick={this.viewAllLinks}
                        >
                            View All Links
                        </a>
                    </div>
                </div>
            </div>
        );
    }
}

export default MainPageComponent;
