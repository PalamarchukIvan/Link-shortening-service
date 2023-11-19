import React, { Component } from 'react'

class HeaderComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {

        }
    }

    render() {
        return (
            <div>
                <header>
                    <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                         <a className="text-white d-block h3 text-primary font-weight-bold ml-5" style={{ textDecoration: 'none' }} href="/">Link-shortening service</a>
                         <a className="text-white d-block h3 text-primary font-weight-bold ml-5" style={{ textDecoration: 'none' }} href="/main">Profile</a>
                    </nav>
                </header>
            </div>
        )
    }
}

export default HeaderComponent