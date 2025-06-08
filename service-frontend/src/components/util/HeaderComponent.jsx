import React, { Component } from 'react';
import UserService from "../../services/UserService";

const LOGOUT_LINK = 'http://localhost:8080/rest/user/logout';

class HeaderComponent extends Component {
    state = {
        loggedIn: false
    };

    componentDidMount() {
        UserService.getCurrentUser()
            .then(() => this.setState({ loggedIn: true }))
            .catch(() => this.setState({ loggedIn: false }));
    }

    doLogout = (e) => {
        e.preventDefault();

        UserService.doLogout().then(
            () => {
                this.setState({loggedIn: false})
                window.location.href = '/login';
            }
        )
    };

    render() {
        return (
            <div>
                <header>
                    <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                        <a
                            className="text-white d-block h3 text-primary font-weight-bold ml-5"
                            style={{ textDecoration: 'none' }}
                            href="/"
                        >
                            Link-shortening service
                        </a>
                        {this.state.loggedIn && (<a
                            className="text-white d-block h3 text-primary font-weight-bold ml-5"
                            style={{textDecoration: 'none'}}
                            href="/main"
                        >
                            Profile
                        </a>)}
                        {this.state.loggedIn && (
                            <a
                                className="text-white d-block h3 text-primary font-weight-bold mr-5 ml-auto"
                                style={{ textDecoration: 'none' }}
                                href="#"
                                onClick={this.doLogout}
                            >
                                Logout
                            </a>
                        )}
                    </nav>
                </header>
            </div>
        );
    }
}

export default HeaderComponent;
