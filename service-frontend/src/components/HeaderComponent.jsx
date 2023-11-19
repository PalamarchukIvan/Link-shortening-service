import React, { Component } from 'react'

class HeaderComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            LOGOUT_LINK: 'http://localhost:8080/logout'
        }
    }
    doLogout() {
        
    }
    
    render() {
        return (
            <div>
                <header>
                    <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                         <a className="text-white d-block h3 text-primary font-weight-bold ml-5" style={{ textDecoration: 'none' }} href="/">Link-shortening service</a>
                         <a className="text-white d-block h3 text-primary font-weight-bold ml-5" style={{ textDecoration: 'none' }} href="/main">Profile</a>
                         <a className="text-white d-block h3 text-primary font-weight-bold mr-5 ml-auto" style={{ textDecoration: 'none' }} href={this.state.LOGOUT_LINK} >Logout</a>
                    </nav>
                </header>
            </div>
        )
    }
}

export default HeaderComponent