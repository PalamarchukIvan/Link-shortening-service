import React, { Component } from "react";
import { Route, Redirect } from "react-router-dom";
import UserService from "../../services/UserService";

export default class PrivateRoute extends Component {
    state = {
        checking: true,
        isAuth: false
    };

    componentDidMount() {
        // call your BE to see if the cookie/session is valid
        UserService.getCurrentUser()
            .then(() => this.setState({ checking: false, isAuth: true }))
            .catch(() => this.setState({ checking: false, isAuth: false }));
    }

    render() {
        const { component: Component, ...rest } = this.props;
        const { checking, isAuth } = this.state;

        if (checking) {
            // you could return a spinner here
            return null;
        }

        return (
            <Route
                {...rest}
                render={props =>
                    isAuth ? (
                        <Component {...props} />
                    ) : (
                        <Redirect
                            to={{
                                pathname: "/login",
                                state: { from: props.location }
                            }}
                        />
                    )
                }
            />
        );
    }
}
