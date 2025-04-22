// src/components/EmailVerificationComponent.jsx
import React, { Component } from "react";
import UserService from "../services/UserService";

export default class EmailVerificationComponent extends Component {
    // guard against duplicate calls in StrictMode
    hasVerified = false;

    state = {
        message: "Verifying…",
        error: false
    };

    async componentDidMount() {
        // run only once
        if (this.hasVerified) return;
        this.hasVerified = true;

        const params = new URLSearchParams(this.props.location.search);
        const token = params.get("token");
        if (!token) {
            this.setState({ message: "Invalid verification link.", error: true });
            return;
        }

        try {
            // backend call will only fire once now
            await UserService.verify(token);
            // on 200 → go to main
            window.location.href = "/main";
        } catch (err) {
            this.setState({
                message:
                    err.response?.data?.errors?.join(", ") ||
                    err.response?.data?.message ||
                    "Verification failed.",
                error: true
            });
        }
    }

    render() {
        const { message, error } = this.state;
        return (
            <div style={{ maxWidth: 400, margin: "2rem auto", textAlign: "center" }}>
                <div className={`alert ${error ? "alert-danger" : "alert-info"}`}>
                    {message}
                </div>
            </div>
        );
    }
}
