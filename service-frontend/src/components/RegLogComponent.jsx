import React, {Component} from 'react';

class RegLogComponent extends Component {
    render() {
        return (
            <div>
                <h1>Welcome!</h1>
                <p>Click <a href="@{/main}">here</a> to see your account.</p>
            </div>
        );
    }
}

export default RegLogComponent;