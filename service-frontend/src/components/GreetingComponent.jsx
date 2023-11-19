import React, {Component} from 'react';
import {Link} from "react-router-dom";

class GreetingComponent extends Component {
    constructor(props) {
        super(props);

        this.gotToMainPage = this.gotToMainPage.bind(this)
    }
    gotToMainPage() {
        console.log("clicked")
    }
    render() {
        return (
            <div>
                <h1>Welcome!</h1>
                <div>
                    <a className="btn btn-primary" onClick={this.gotToMainPage} href="/main">visit your acc</a>
                </div>
            </div>
        );
    }
}

export default GreetingComponent;