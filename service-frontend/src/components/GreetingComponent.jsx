import React, { Component } from 'react';
import { Link } from "react-router-dom";

class GreetingComponent extends Component {
    constructor(props) {
        super(props);
        this.goToMainPage = this.goToMainPage.bind(this);
    }

    goToMainPage() {
        console.log("clicked");
        this.props.history.push("/main");
    }

    render() {
        return (
            <div>
                <div className="jumbotron">
                    <h1 className="display-4">Link Shortening Service</h1>
                    <p className="lead">
                        Make your URLs shorter and more convenient with our service.
                    </p>
                    <hr className="my-4" />
                    <p>Just sign in or register to start using the service!</p>
                    <p className="lead">
                        <Link className="btn btn-primary btn-lg" to="/login">
                            Sign In
                        </Link>
                        &nbsp;
                        <Link className="btn btn-secondary btn-lg" to="/register" role="button">
                            Register
                        </Link>
                    </p>
                </div>

                <div className="container text-center">
                    <h2 className="mb-4">Our Advantages</h2>
                    <div className="row">
                        <div className="col-md-4 mb-4">
                            <div className="animated fadeInDown delay-1s">
                                <i className="fas fa-link fa-3x mb-2"></i>
                                <h4>Short Links</h4>
                                <p>Make your URLs concise and easy to share.</p>
                            </div>
                        </div>
                        <div className="col-md-4 mb-4">
                            <div className="animated fadeInDown delay-2s">
                                <i className="fas fa-chart-line fa-3x mb-2"></i>
                                <h4>Analytics</h4>
                                <p>Track click statistics for your shortened links.</p>
                            </div>
                        </div>
                        <div className="col-md-4 mb-4">
                            <div className="animated fadeInDown delay-3s">
                                <i className="fas fa-lock fa-3x mb-2"></i>
                                <h4>Security</h4>
                                <p>Your data is protected and kept safe.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default GreetingComponent;
