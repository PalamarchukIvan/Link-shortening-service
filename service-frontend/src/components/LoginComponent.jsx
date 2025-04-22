// src/components/LoginComponent.jsx
import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import UserService from "../services/UserService";

class LoginComponent extends Component {
    state = {
        username: "",
        password: "",
        error: ""
    };

    handleChange = e =>
        this.setState({ [e.target.name]: e.target.value, error: "" });

    handleSubmit = async e => {
        e.preventDefault();
        try {
            await UserService.doLogin({
                username: this.state.username,
                password: this.state.password
            });
            window.location.href = "/main";
            
        } catch (err) {
            this.setState({
                error:
                    err.response?.data?.message ||
                    "Login failed—please check your credentials"
            });
        }
    };

    goToRegister = () => {
        window.location.href = "/register";
    };

    render() {
        return (
            <div className="login-container" style={{ maxWidth: 360, margin: "2rem auto" }}>
                <h2 className="mb-4">Sign In</h2>

                {this.state.error && (
                    <div className="alert alert-danger">{this.state.error}</div>
                )}

                <form onSubmit={this.handleSubmit}>
                    <div className="form-group mb-3">
                        <label>Username</label>
                        <input
                            name="username"
                            value={this.state.username}
                            onChange={this.handleChange}
                            className="form-control"
                            placeholder="Enter username"
                            required
                        />
                    </div>

                    <div className="form-group mb-4">
                        <label>Password</label>
                        <input
                            type="password"
                            name="password"
                            value={this.state.password}
                            onChange={this.handleChange}
                            className="form-control"
                            placeholder="Enter password"
                            required
                        />
                    </div>

                    <div className="d-flex justify-content-between">
                        <button type="submit" className="btn btn-primary">
                            Sign In
                        </button>
                        <button
                            type="button"
                            className="btn btn-secondary"
                            onClick={this.goToRegister}
                        >
                            Register
                        </button>
                    </div>
                </form>
            </div>
        );
    }
}

export default withRouter(LoginComponent);
