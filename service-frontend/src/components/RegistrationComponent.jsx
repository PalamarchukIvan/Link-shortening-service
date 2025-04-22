import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import UserService from "../services/UserService";

class RegistrationComponent extends Component {
    state = {
        email: "",
        name: "",
        password: "",
        passwordRepeat: "",
        errors: [],
        success: false
    };

    handleChange = e =>
        this.setState({ [e.target.name]: e.target.value, errors: [] });

    validate = () => {
        const errs = [];
        const { email, name, password, passwordRepeat } = this.state;
        const emailRegex = /^\S+@\S+\.\S+$/;

        if (!emailRegex.test(email)) {
            errs.push("Invalid email address.");
        }
        if (name.trim().length === 0) {
            errs.push("User name is required.");
        }
        if (password.length < 6) {
            errs.push("Password must be at least 6 characters.");
        }
        if (password !== passwordRepeat) {
            errs.push("Passwords do not match.");
        }
        return errs;
    };

    register = async e => {
        e.preventDefault();
        const errs = this.validate();
        if (errs.length > 0) {
            return this.setState({ errors: errs });
        }

        try {
            await UserService.doRegistration({
                username: this.state.email,
                name: this.state.name,
                password: this.state.password
            });

            this.setState({
                success: true,
                errors: []
            });
        } catch (error) {
            const respErrs =
                error.response?.data?.errors ||
                [error.response?.data?.message] ||
                ["Registration failed"];
            this.setState({ errors: respErrs });
        }
    };

    render() {
        const { email, name, password, passwordRepeat, errors, success } = this.state;

        if (success) {
            return (
                <div className="alert alert-info" style={{ maxWidth: 400, margin: "2rem auto" }}>
                    Registration successful!<br/>
                    Please go to your email and click the verification link.
                </div>
            );
        }

        return (
            <div style={{ maxWidth: 400, margin: "2rem auto" }}>
                <h2 className="mb-4">Register</h2>

                {errors.length > 0 && (
                    <div className="alert alert-danger">
                        <ul className="mb-0">
                            {errors.map((msg, i) => <li key={i}>{msg}</li>)}
                        </ul>
                    </div>
                )}

                <form onSubmit={this.register}>
                    <div className="form-group mb-3">
                        <label>Email</label>
                        <input
                            name="email"
                            type="email"
                            value={email}
                            onChange={this.handleChange}
                            className="form-control"
                            placeholder="you@example.com"
                            required
                        />
                    </div>
                    <div className="form-group mb-3">
                        <label>User Name</label>
                        <input
                            name="name"
                            value={name}
                            onChange={this.handleChange}
                            className="form-control"
                            placeholder="Your display name"
                            required
                        />
                    </div>
                    <div className="form-group mb-3">
                        <label>Password</label>
                        <input
                            name="password"
                            type="password"
                            value={password}
                            onChange={this.handleChange}
                            className="form-control"
                            placeholder="At least 6 characters"
                            required
                        />
                    </div>
                    <div className="form-group mb-4">
                        <label>Repeat Password</label>
                        <input
                            name="passwordRepeat"
                            type="password"
                            value={passwordRepeat}
                            onChange={this.handleChange}
                            className="form-control"
                            placeholder="Repeat your password"
                            required
                        />
                    </div>

                    <button type="submit" className="btn btn-primary w-100">
                        Register
                    </button>
                </form>
            </div>
        );
    }
}

export default withRouter(RegistrationComponent);
