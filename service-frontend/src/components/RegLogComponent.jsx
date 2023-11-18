import React, {Component} from 'react';

class RegLogComponent extends Component {
    constructor(props) {
        super(props);

        this.gotToMainPage = this.gotToMainPage.bind(this)
    }
    gotToMainPage() {
        this.props.history.push('/main')
    }
    render() {
        return (
            <div>
                <h1>Welcome!</h1>
                <div>
                    <a href="/main">visit your acc</a>
                    <button className="btn btn-primary" onClick={this.gotToMainPage}>Visit your account</button>
                </div>
            </div>
        );
    }
}

export default RegLogComponent;